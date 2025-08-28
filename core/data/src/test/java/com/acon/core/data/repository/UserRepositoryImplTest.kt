package com.acon.core.data.repository

import com.acon.acon.domain.error.user.PostSignInError
import com.acon.acon.domain.error.user.PostSignOutError
import com.acon.acon.domain.repository.UserRepository
import com.acon.core.data.datasource.local.TokenLocalDataSource
import com.acon.core.data.datasource.remote.UserRemoteDataSource
import com.acon.core.data.dto.response.SignInResponse
import com.acon.core.data.error.RemoteError
import com.acon.core.data.session.SessionHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.reflect.KClass
import kotlin.test.assertNotNull
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
    fun `유저 상태는 SessionHandler로 부터 넘겨 받는다`() {
        // When
        userRepository.getUserType()

        // Then
        coVerify(exactly = 1) { sessionHandler.getUserType() }
    }

    @Test
    fun `로그인 API 성공 시, 로그인 세션 정보를 반영한다`() = runTest {
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
        coVerify(exactly = 1) { sessionHandler.completeSignIn(signInResponse.accessToken!!, signInResponse.refreshToken!!) }
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
}