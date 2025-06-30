package com.acon.acon.data.session

import app.cash.turbine.test
import com.acon.acon.core.analytics.amplitude.AconAmplitude
import com.acon.acon.core.model.type.UserType
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class SessionHandlerImplTest {

    @RelaxedMockK
    lateinit var tokenLocalDataSource: TokenLocalDataSource

    private lateinit var testScope: TestScope

    private lateinit var sessionHandler: SessionHandler

    @BeforeEach
    fun setUp() {
        testScope = TestScope()
        sessionHandler = SessionHandlerImpl(tokenLocalDataSource, testScope)
    }

    @AfterEach
    fun tearDown() {
        testScope.cancel()
    }

    @Test
    fun `로그인 완료 시, 액세스 토큰, 리프레시 토큰을 저장한다`() = runTest {
        // Given
        val fakeAccessToken = "Fake Access Token"
        val fakeRefreshToken = "Fake Refresh Token"

        // When
        sessionHandler.completeSignIn(fakeAccessToken, fakeRefreshToken)

        // Then
        coVerify(exactly = 1) { tokenLocalDataSource.saveAccessToken(fakeAccessToken) }
        coVerify(exactly = 1) { tokenLocalDataSource.saveRefreshToken(fakeRefreshToken) }
    }

    @Test
    fun `로그인 완료 시, 유저 상태를 USER로 설정한다`() = runTest {
        // Given
        val fakeAccessToken = "Fake Access Token"
        val fakeRefreshToken = "Fake Refresh Token"

        // When & Then
        sessionHandler.getUserType().test {
            assertEquals(UserType.GUEST, awaitItem(), "초기 상태는 GUEST여야 합니다.")

            sessionHandler.completeSignIn(fakeAccessToken, fakeRefreshToken)

            assertEquals(UserType.USER, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `로컬에 액세스 토큰 존재 시, 앱의 유저 상태를 USER로 설정한다`() = runTest {
        // Given
        sessionHandler = SessionHandlerImpl(tokenLocalDataSource, this)
        coEvery { tokenLocalDataSource.getAccessToken() } returns "Dummy Access Token"

        // When
        advanceUntilIdle()

        // Then
        val finalUserType = sessionHandler.getUserType().first()
        assertEquals(UserType.USER, finalUserType)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `로컬에 액세스 토큰이 없을 시, 앱의 유저 상태를 GUEST로 설정한다`() = runTest {
        // Given
        sessionHandler = SessionHandlerImpl(tokenLocalDataSource, this)

        coEvery { tokenLocalDataSource.getAccessToken() } returns null

        // When
        advanceUntilIdle()

        // Then
        val finalUserType = sessionHandler.getUserType().first()
        assertEquals(UserType.GUEST, finalUserType)
    }

    @Test
    fun `세션 초기화 함수는 로컬에 저장된 토큰을 제거한다`() = runTest {
        // When
        sessionHandler.clearSession()

        // Then
        coVerify(exactly = 1) { tokenLocalDataSource.removeAllTokens() }
    }

    @Test
    fun `세션 초기화 함수는 앱의 유저 상태를 GUEST로 설정한다`() = runTest {
        // When & Then
        sessionHandler.getUserType().test {
            sessionHandler.clearSession()

            assertEquals(UserType.GUEST, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `세션 초기화 함수는 Amplitude 유저 ID를 초기화한다`() = runTest {
        // Given
        mockkObject(AconAmplitude)
        // When
        sessionHandler.clearSession()

        // Then
        verify(exactly = 1) { AconAmplitude.clearUserId() }

        unmockkObject(AconAmplitude)
    }
}