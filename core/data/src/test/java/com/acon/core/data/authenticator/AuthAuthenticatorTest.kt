package com.acon.core.data.authenticator

import android.content.Context
import com.acon.acon.core.launcher.AppLauncher
import com.acon.acon.data.dto.request.DeleteAccountRequest
import com.acon.acon.domain.error.user.ReissueError
import com.acon.core.data.api.remote.noauth.UserNoAuthApi
import com.acon.core.data.assertValidErrorMapping
import com.acon.core.data.authentication.AuthAuthenticator
import com.acon.core.data.createFakeRemoteError
import com.acon.core.data.datasource.local.TokenLocalDataSource
import com.acon.core.data.dto.request.ReissueRequest
import com.acon.core.data.dto.request.SignOutRequest
import com.acon.core.data.dto.response.TokenResponse
import com.acon.core.data.error.runCatchingWith
import com.acon.core.data.session.SessionHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import okio.Buffer
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AuthAuthenticatorTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    lateinit var mockContext: Context

    @RelaxedMockK
    lateinit var tokenLocalDataSource: TokenLocalDataSource

    @RelaxedMockK
    lateinit var sessionHandler: SessionHandler

    @MockK
    lateinit var userNoAuthApi: UserNoAuthApi

    @RelaxedMockK
    lateinit var launcher: AppLauncher

    @InjectMockKs
    lateinit var authAuthenticator: AuthAuthenticator

    private val route: Route = mockk<Route>(relaxed = true)
    private val mockResponse = mockk<Response>(relaxed = true)

    @Test
    fun `401에러 발생 시, 로컬에 존재하는 리프레시 토큰을 불러온다`() {
        // Given

        // When
        authAuthenticator.authenticate(route, mockResponse)

        // Then
        coVerify(exactly = 1) { tokenLocalDataSource.getRefreshToken() }
    }

    @Test
    fun `로컬의 리프레시 토큰이 없거나 빈 값일 경우 앱을 재시작한다`() {
        // Given
        coEvery { tokenLocalDataSource.getRefreshToken() } returns ""

        // When
        authAuthenticator.authenticate(route, mockResponse)

        // Then
        verify(exactly = 1) { launcher.restartApp(mockContext) }
    }

    @Test
    fun `로컬에 리프레시 토큰이 존재할 경우, 해당 토큰으로 새 토큰 갱신을 시도한다`() = runTest {
        // Given
        val localRefreshTokenRequest = givenLocalRefreshTokenRequest()

        // When
        authAuthenticator.authenticate(route, mockResponse)

        // Then
        coVerify(exactly = 1) { userNoAuthApi.reissueToken(localRefreshTokenRequest) }
    }

    @Test
    fun `로컬에 저장된 리프레시 토큰으로 새 토큰 갱신에 성공했을 경우, 토큰을 로컬에 저장한다`() = runTest {
        // Given
        val localRefreshTokenRequest = givenLocalRefreshTokenRequest()
        coEvery { userNoAuthApi.reissueToken(localRefreshTokenRequest) } returns TokenResponse(
            accessToken = "New Access Token", refreshToken = "New Refresh Token"
        )

        // When
        authAuthenticator.authenticate(route, mockResponse)

        // Then
        coVerify(exactly = 1) { tokenLocalDataSource.saveAccessToken("New Access Token") }
        coVerify(exactly = 1) { tokenLocalDataSource.saveRefreshToken("New Refresh Token") }
    }

    @Test
    fun `로컬에 저장된 리프레시 토큰으로 새 토큰 갱신에 성공했을 경우, 앱을 재시작 해선 안된다`() = runTest {
        // Given
        val localRefreshTokenRequest = givenLocalRefreshTokenRequest()
        coEvery { userNoAuthApi.reissueToken(localRefreshTokenRequest) } returns TokenResponse(
            accessToken = "New Access Token", refreshToken = "New Refresh Token"
        )

        // When
        authAuthenticator.authenticate(route, mockResponse)

        // Then
        coVerify(exactly = 0) { launcher.restartApp(any()) }
    }

    @Test
    fun `로컬에 저장된 리프레시 토큰으로 새 토큰 갱신에 실패했을 경우, 앱을 재시작 한다`() = runTest {
        // Given
        val localRefreshTokenRequest = givenLocalRefreshTokenRequest()
        coEvery { userNoAuthApi.reissueToken(localRefreshTokenRequest) } throws Exception()

        // When
        authAuthenticator.authenticate(route, mockResponse)

        // Then
        coVerify(exactly = 1) { launcher.restartApp(mockContext) }
    }

    @Test
    fun `authenticate는 최종 성공했을 경우 새 액세스 토큰이 부착된 새 Request를 반환한다`() = runTest {
        // Given
        val realRequest = Request.Builder().url("https://test").build()
        every { mockResponse.request } returns realRequest

        val localRefreshTokenRequest = givenLocalRefreshTokenRequest()
        coEvery { userNoAuthApi.reissueToken(localRefreshTokenRequest) } returns TokenResponse(
            accessToken = "New Access Token", refreshToken = "New Refresh Token"
        )

        // When
        val newRequest = authAuthenticator.authenticate(route, mockResponse)

        // Then
        assertEquals("Bearer New Access Token", newRequest?.header("Authorization"))
    }

    @Test
    fun `저장된 리프레시 토큰이 없으면 유저정보 Session을 초기화한다`() = runTest {
        // Given
        coEvery { tokenLocalDataSource.getRefreshToken() } returns null

        // When
        val newRequest = authAuthenticator.authenticate(route, mockResponse)

        // Then
        assertNull(newRequest)
        coVerify { sessionHandler.clearSession() }
    }

    @Test
    fun `토큰 갱신 API가 실패하면 유저정보 Session을 초기화한다`() = runTest {
        // Given
        val localRefreshTokenRequest = givenLocalRefreshTokenRequest()
        coEvery { userNoAuthApi.reissueToken(localRefreshTokenRequest) } throws Exception()

        // When
        val newRequest = authAuthenticator.authenticate(route, mockResponse)

        // Then
        assertNull(newRequest)
        coVerify { sessionHandler.clearSession() }
    }


    @Test
    fun `토큰 갱신 API의 응답 값이 유효하지 않으면 유저정보 Session을 초기화한다`() = runTest {
        // Given
        val localRefreshTokenRequest = givenLocalRefreshTokenRequest()
        coEvery { userNoAuthApi.reissueToken(localRefreshTokenRequest) } returns TokenResponse(null, null)

        // When
        val newRequest = authAuthenticator.authenticate(route, mockResponse)

        // Then
        assertNull(newRequest)
        coVerify { sessionHandler.clearSession() }
    }

    @Test
    fun `저장된 리프레시 토큰이 유효하지 않으면 해당하는 실패 에러 객체를 반환한다`() = runTest {
        // Given
        coEvery { tokenLocalDataSource.getRefreshToken() } returns "Fake Invalid Refresh Token"
        coEvery { userNoAuthApi.reissueToken(any()) } throws createFakeRemoteError(40088)

        // When
        authAuthenticator.authenticate(route, mockResponse)
        val result = runCatchingWith(ReissueError()) { userNoAuthApi.reissueToken(mockk()) }

        // Then
        assertValidErrorMapping(result, ReissueError.InvalidRefreshToken::class)
    }

    @Test
    fun `로그아웃 API에서 발생한 Authentication일 경우, 요청 Body를 새 리프레시 토큰으로 교체한다`() = runTest {
        // Given
        coEvery { tokenLocalDataSource.getRefreshToken() } returns "Dummy Old Refresh Token"
        coEvery { userNoAuthApi.reissueToken(any()) } returns TokenResponse("New Access Token", "New Refresh Token")
        val mockRequest = Request.Builder()
            .url("https://acon.com/api/auth/logout")
            .header("Authorization", "Bearer Dummy-expired_access_token")
            .method("POST", mockk())
            .build()
        every { mockResponse.request } returns mockRequest

        // When
        val newRequest = authAuthenticator.authenticate(route, mockResponse)
        val newRefreshToken = userNoAuthApi.reissueToken(mockk()).refreshToken!!

        val expectedBody = Json.encodeToString(SignOutRequest(newRefreshToken))
        val actualBody = newRequest?.body.asString()

        // Then
        assertNotNull(actualBody)
        assertEquals(actualBody, expectedBody)
    }

    @Test
    fun `회원탈퇴 API에서 발생한 Authentication일 경우, 요청 Body를 새 리프레시 토큰으로 교체한다`() = runTest {
        // Given
        val dummyOldRefreshToken = "Dummy Old Refresh Token"
        coEvery { tokenLocalDataSource.getRefreshToken() } returns dummyOldRefreshToken

        val fakeReason = "그냥 탈퇴함"
        val fakeBody = Json.encodeToString(DeleteAccountRequest(fakeReason, dummyOldRefreshToken)).toRequestBody()

        coEvery { userNoAuthApi.reissueToken(any()) } returns TokenResponse("New Access Token", "New Refresh Token")
        val mockRequest = Request.Builder()
            .url("https://acon.com/api/members/withdrawal")
            .header("Authorization", "Bearer Dummy-expired_access_token")
            .method("POST", fakeBody)
            .build()
        every { mockResponse.request } returns mockRequest

        // When
        val newRequest = authAuthenticator.authenticate(route, mockResponse)
        val newRefreshToken = userNoAuthApi.reissueToken(mockk()).refreshToken!!

        val expectedBody = Json.encodeToString(DeleteAccountRequest(fakeReason, newRefreshToken))
        val actualBody = newRequest?.body.asString()

        // Then
        assertNotNull(actualBody)
        assertEquals(actualBody, expectedBody)
    }

    private suspend fun givenLocalRefreshTokenRequest(): ReissueRequest {
        coEvery { tokenLocalDataSource.getRefreshToken() } returns "Local Refresh Token"
        val localRefreshToken = tokenLocalDataSource.getRefreshToken()!!
        return ReissueRequest(localRefreshToken)
    }

    private fun RequestBody?.asString(): String? {
        if (this == null) return null
        return try {
            val buffer = Buffer()
            this.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            null
        }
    }
}