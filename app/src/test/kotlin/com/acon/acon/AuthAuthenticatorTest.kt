package com.acon.acon

import android.content.Context
import com.acon.acon.authentication.AuthAuthenticator
import com.acon.acon.data.SessionManager
import com.acon.acon.data.api.remote.ReissueTokenApi
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.dto.request.RefreshRequest
import com.acon.acon.data.dto.response.RefreshResponse
import com.acon.acon.navigator.AppNavigator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AuthAuthenticatorTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    lateinit var mockContext: Context

    @RelaxedMockK
    lateinit var tokenLocalDataSource: TokenLocalDataSource

    @RelaxedMockK
    lateinit var sessionManager: SessionManager

    @RelaxedMockK
    lateinit var reissueTokenApi: ReissueTokenApi

    @RelaxedMockK
    lateinit var navigator: AppNavigator

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
        verify(exactly = 1) { navigator.restartApp(mockContext) }
    }

    @Test
    fun `로컬에 리프레시 토큰이 존재할 경우, 해당 토큰으로 새 토큰 갱신을 시도한다`() = runTest {
        // Given
        val localRefreshTokenRequest = givenLocalRefreshTokenRequest()

        // When
        authAuthenticator.authenticate(route, mockResponse)

        // Then
        coVerify(exactly = 1) { reissueTokenApi.postRefresh(localRefreshTokenRequest) }
    }

    @Test
    fun `로컬에 저장된 리프레시 토큰으로 새 토큰 갱신에 성공했을 경우, 토큰을 로컬에 저장한다`() = runTest {
        // Given
        val localRefreshTokenRequest = givenLocalRefreshTokenRequest()
        coEvery { reissueTokenApi.postRefresh(localRefreshTokenRequest) } returns RefreshResponse(
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
        coEvery { reissueTokenApi.postRefresh(localRefreshTokenRequest) } returns RefreshResponse(
            accessToken = "New Access Token", refreshToken = "New Refresh Token"
        )

        // When
        authAuthenticator.authenticate(route, mockResponse)

        // Then
        coVerify(exactly = 0) { navigator.restartApp(any()) }
    }

    @Test
    fun `로컬에 저장된 리프레시 토큰으로 새 토큰 갱신에 실패했을 경우, 앱을 재시작 한다`() = runTest {
        // Given
        val localRefreshTokenRequest = givenLocalRefreshTokenRequest()
        coEvery { reissueTokenApi.postRefresh(localRefreshTokenRequest) } throws Exception()

        // When
        authAuthenticator.authenticate(route, mockResponse)

        // Then
        coVerify(exactly = 1) { navigator.restartApp(mockContext) }
    }

    @Test
    fun `authenticate는 최종 성공했을 경우 새 액세스 토큰이 부착된 새 Request를 반환한다`() = runTest {
        // Given
        val realRequest = Request.Builder().url("https://test").build()
        every { mockResponse.request } returns realRequest

        val localRefreshTokenRequest = givenLocalRefreshTokenRequest()
        coEvery { reissueTokenApi.postRefresh(localRefreshTokenRequest) } returns RefreshResponse(
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
        coVerify { sessionManager.clearSession() }
    }

    @Test
    fun `토큰 갱신 API가 실패하면 유저정보 Session을 초기화한다`() = runTest {
        // Given
        val localRefreshTokenRequest = givenLocalRefreshTokenRequest()
        coEvery { reissueTokenApi.postRefresh(localRefreshTokenRequest) } throws Exception()

        // When
        val newRequest = authAuthenticator.authenticate(route, mockResponse)

        // Then
        assertNull(newRequest)
        coVerify { sessionManager.clearSession() }
    }


    @Test
    fun `토큰 갱신 API의 응답 값이 유효하지 않으면 유저정보 Session을 초기화한다`() = runTest {
        // Given
        val localRefreshTokenRequest = givenLocalRefreshTokenRequest()
        coEvery { reissueTokenApi.postRefresh(localRefreshTokenRequest) } returns RefreshResponse(null, null)

        // When
        val newRequest = authAuthenticator.authenticate(route, mockResponse)

        // Then
        assertNull(newRequest)
        coVerify { sessionManager.clearSession() }
    }

    private suspend fun givenLocalRefreshTokenRequest(): RefreshRequest {
        coEvery { tokenLocalDataSource.getRefreshToken() } returns "Local Refresh Token"
        val localRefreshToken = tokenLocalDataSource.getRefreshToken()
        return RefreshRequest(localRefreshToken)
    }
}