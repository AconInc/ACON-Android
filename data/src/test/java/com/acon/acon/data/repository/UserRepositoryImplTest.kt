package com.acon.acon.data.repository

import app.cash.turbine.test
import com.acon.acon.core.analytics.amplitude.AconAmplitude
import com.acon.acon.core.model.type.UserType
import com.acon.acon.data.SessionHandler
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.datasource.remote.UserRemoteDataSource
import com.acon.acon.data.dto.response.SignInResponse
import com.acon.acon.data.error.RemoteError
import com.acon.acon.domain.error.user.PostSignOutError
import com.acon.acon.domain.error.user.PostSignInError
import com.acon.acon.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import java.util.stream.Stream
import kotlin.reflect.KClass
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class UserRepositoryImplTest {

    @RelaxedMockK
    lateinit var userRemoteDataSource: UserRemoteDataSource

    @RelaxedMockK
    lateinit var tokenLocalDataSource: TokenLocalDataSource

    @RelaxedMockK
    lateinit var sessionHandler: SessionHandler

    private lateinit var testScope: TestScope

    private lateinit var userRepository: UserRepository

    companion object {
        @JvmStatic
        fun postSignInErrorScenarios(): Stream<Arguments> = Stream.of(
            Arguments.of(40009, PostSignInError.InvalidSocialType::class),
            Arguments.of(40010, PostSignInError.InvalidIdTokenSignature::class),
            Arguments.of(50002, PostSignInError.GooglePublicKeyDownloadFailed::class)
        )

        @JvmStatic
        fun postSignOutErrorScenarios(): Stream<Arguments> = Stream.of(
            Arguments.of(40088, PostSignOutError.InvalidRefreshToken::class)
        )
    }

    @BeforeEach
    fun setUp() {
        testScope = TestScope()
        userRepository = UserRepositoryImpl(userRemoteDataSource, tokenLocalDataSource, sessionHandler)
    }

    @AfterEach
    fun tearDown() {
        testScope.cancel()
    }

    @Test
    fun `로그인 API 성공 시, 응답받은 액세스와 리프레시 토큰을 로컬에 저장한다`() = runTest {
        // Given
        val signInResponse = SignInResponse(
            externalUUID = "Dummy UUID",
            accessToken = "New Access Token",
            refreshToken = "New Refresh Token",
            hasVerifiedArea = true
        )
        coEvery { userRemoteDataSource.signIn(any()) } returns signInResponse

        // When
        userRepository.signIn(mockk(), "Dummy Token")

        // Then
        assertNotNull(signInResponse.accessToken)
        assertNotNull(signInResponse.refreshToken)
        coVerify(exactly = 1) { tokenLocalDataSource.saveAccessToken(signInResponse.accessToken!!) }
        coVerify(exactly = 1) { tokenLocalDataSource.saveRefreshToken(signInResponse.refreshToken!!) }
    }

    @Test
    fun `로그인 API 성공 시, 앱의 유저 상태를 USER로 설정한다`() = runTest {
        // Given
        val signInResponse = SignInResponse(
            externalUUID = "Dummy UUID",
            accessToken = "New Access Token",
            refreshToken = "New Refresh Token",
            hasVerifiedArea = true
        )
        coEvery { userRemoteDataSource.signIn(any()) } returns signInResponse
        coEvery { tokenLocalDataSource.getAccessToken() } returns null

        // When & Then
        userRepository.getUserType().test {
            assertEquals(UserType.GUEST, awaitItem(), "초기 상태는 GUEST여야 합니다.")

            userRepository.signIn(mockk(), "Dummy Token")

            assertEquals(UserType.USER, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @ParameterizedTest
    @MethodSource("postSignInErrorScenarios")
    fun `로그인 API 실패 시, 에러 코드에 대응되는 올바른 로그인 실패 예외 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<PostSignInError>
    ) = runTest {
        // Given
        val fakeRemoteError = RemoteError(mockk(relaxed = true), errorCode, "")
        coEvery { userRemoteDataSource.signIn(any()) } throws fakeRemoteError

        // When
        val result = userRepository.signIn(mockk(), "Dummy Token")

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertInstanceOf(expectedErrorClass.java, exception, "에러 코드와 예외 클래스가 올바르게 매핑되지 않음")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `로컬에 액세스 토큰 존재 시, 앱의 유저 상태를 USER로 설정한다`() = runTest {
        // Given
        userRepository = UserRepositoryImpl(userRemoteDataSource, tokenLocalDataSource, sessionHandler)
        coEvery { tokenLocalDataSource.getAccessToken() } returns "Dummy Access Token"

        // When
        advanceUntilIdle()

        // Then
        val finalUserType = userRepository.getUserType().first()
        assertEquals(UserType.USER, finalUserType)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `로컬에 액세스 토큰 없을 시, 앱의 유저 상태를 USER로 설정한다`() = runTest {
        // Given
        userRepository = UserRepositoryImpl(userRemoteDataSource, tokenLocalDataSource, sessionHandler)
        coEvery { tokenLocalDataSource.getAccessToken() } returns null

        // When
        advanceUntilIdle()

        // Then
        val finalUserType = userRepository.getUserType().first()
        assertEquals(UserType.GUEST, finalUserType)
    }

    @Test
    fun `로그아웃 API 성공 시 세션을 초기화한다`() = runTest {
        // Given
        coEvery { userRemoteDataSource.signOut(any()) } returns mockk()

        // When
        userRepository.signOut()

        // Then
        coVerify(exactly = 1) { userRepository.clearSession() }
    }

    @ParameterizedTest
    @MethodSource("postSignOutErrorScenarios")
    fun `로그아웃 API 실패 시, 에러 코드에 대응되는 올바른 로그아웃 실패 예외 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<PostSignOutError>
    ) = runTest {
        // Given
        val fakeRemoteError = RemoteError(mockk(relaxed = true), errorCode, "")
        coEvery { userRemoteDataSource.signOut(any()) } throws fakeRemoteError

        // When
        val result = userRepository.signOut()

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertInstanceOf(expectedErrorClass.java, exception, "에러 코드와 예외 클래스가 올바르게 매핑되지 않음")
    }

    @Test
    fun `로그아웃 API 실패 시 세션을 초기화하지 않는다`() = runTest {
        // Given
        coEvery { userRemoteDataSource.signOut(any()) } throws mockk()

        // When
        userRepository.signOut()

        // Then
        coVerify(exactly = 0) { userRepository.clearSession() }
    }

    @Test
    fun `회원탈퇴 성공 시 세션을 초기화한다`() = runTest {
        // Given
        coEvery { userRemoteDataSource.deleteAccount(any()) } returns mockk()

        // When
        userRepository.deleteAccount("Dummy Reason")

        // Then
        coVerify(exactly = 1) { userRepository.clearSession() }
    }


    @Test
    fun `회원탈퇴 실패 시 세션을 초기화하지 않는다`() = runTest {
        // Given
        coEvery { userRemoteDataSource.deleteAccount(any()) } throws mockk()

        // When
        userRepository.deleteAccount("Dummy Reason")

        // Then
        coVerify(exactly = 0) { userRepository.clearSession() }
    }

    @Test
    fun `세션 초기화 함수는 로컬에 저장된 토큰을 제거한다`() = runTest {
        // When
        userRepository.clearSession()

        // Then
        coVerify(exactly = 1) { tokenLocalDataSource.removeAllTokens() }
    }

    @Test
    fun `세션 초기화 함수는 앱의 유저 상태를 GUEST로 설정한다`() = runTest {
        // Given
        val signInResponse = SignInResponse(
            externalUUID = "Dummy UUID",
            accessToken = "New Access Token",
            refreshToken = "New Refresh Token",
            hasVerifiedArea = true
        )
        coEvery { userRemoteDataSource.signIn(any()) } returns signInResponse

        userRepository.signIn(mockk(), "Dummy Token")

        // When & Then
        userRepository.getUserType().test {
            assertEquals(UserType.USER, awaitItem(), "테스트 시작 전 상태는 USER여야 합니다.")

            userRepository.clearSession()

            assertEquals(UserType.GUEST, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `세션 초기화 함수는 Amplitude 유저 ID를 초기화한다`() = runTest {
        // Given
        mockkObject(AconAmplitude)
        // When
        userRepository.clearSession()

        // Then
        verify(exactly = 1) { AconAmplitude.clearUserId() }

        unmockkObject(AconAmplitude)
    }
}