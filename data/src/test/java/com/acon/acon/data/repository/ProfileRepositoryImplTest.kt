package com.acon.acon.data.repository

import com.acon.acon.data.assertValidErrorMapping
import com.acon.acon.data.cache.ProfileInfoCache
import com.acon.acon.data.createErrorStream
import com.acon.acon.data.createFakeRemoteError
import com.acon.acon.data.datasource.remote.ProfileRemoteDataSource
import com.acon.acon.domain.error.profile.ValidateNicknameError
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.reflect.KClass

@ExtendWith(MockKExtension::class)
class ProfileRepositoryImplTest {

    @RelaxedMockK
    lateinit var profileRemoteDataSource: ProfileRemoteDataSource

    @RelaxedMockK
    lateinit var profileInfoCache: ProfileInfoCache

    private lateinit var testScope: TestScope

    lateinit var profileRepositoryImpl: ProfileRepositoryImpl

    companion object {
        @JvmStatic
        fun validNicknameErrorScenarios() = createErrorStream(
            40051 to ValidateNicknameError.UnsatisfiedCondition::class,
            40901 to ValidateNicknameError.AlreadyUsedNickname::class
        )
    }

    @BeforeEach
    fun setUp() {
        testScope = TestScope()
        profileRepositoryImpl = ProfileRepositoryImpl(testScope, profileRemoteDataSource, profileInfoCache)
    }

    @AfterEach
    fun tearDown() {
        testScope.cancel()
    }

    @ParameterizedTest
    @MethodSource("validNicknameErrorScenarios")
    fun `닉네임 유효성 검사 API 실패 시 에러 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<ValidateNicknameError>
    ) = runTest {
        // Given
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { profileRemoteDataSource.validateNickname(any()) } throws fakeRemoteError

        // When
        val result = profileRepositoryImpl.validateNickname("")

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }
}