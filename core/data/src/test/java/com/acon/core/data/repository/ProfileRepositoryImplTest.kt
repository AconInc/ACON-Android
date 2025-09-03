package com.acon.core.data.repository

import com.acon.acon.domain.error.area.DeleteVerifiedAreaError
import com.acon.acon.domain.error.area.ReplaceVerifiedArea
import com.acon.acon.domain.error.profile.SaveSpotError
import com.acon.acon.domain.error.profile.ValidateNicknameError
import com.acon.core.data.assertValidErrorMapping
import com.acon.core.data.cache.ProfileInfoCacheLegacy
import com.acon.core.data.createErrorStream
import com.acon.core.data.createFakeRemoteError
import com.acon.core.data.datasource.remote.ProfileRemoteDataSourceLegacy
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
    lateinit var profileRemoteDataSourceLegacy: ProfileRemoteDataSourceLegacy

    @RelaxedMockK
    lateinit var profileInfoCacheLegacy: ProfileInfoCacheLegacy

    private lateinit var testScope: TestScope

    lateinit var profileRepositoryImpl: ProfileRepositoryLegacyImpl

    companion object {
        @JvmStatic
        fun validNicknameErrorScenarios() = createErrorStream(
            40051 to ValidateNicknameError.UnsatisfiedCondition::class,
            40901 to ValidateNicknameError.AlreadyUsedNickname::class
        )
        @JvmStatic
        fun saveSpotErrorScenarios() = createErrorStream(
            40403 to SaveSpotError.NotExistSpot::class
        )
        @JvmStatic
        fun replaceVerifiedAreaErrorScenarios() = createErrorStream(
            40012 to ReplaceVerifiedArea.OutOfServiceAreaError::class,
            40054 to ReplaceVerifiedArea.InvalidVerifiedArea::class,
            40055 to ReplaceVerifiedArea.PeriodRestrictedDeleteError::class,
            40056 to ReplaceVerifiedArea.MultiLocationReplaceError::class,
            40404 to ReplaceVerifiedArea.VerifiedAreaNotFound::class
        )
        @JvmStatic
        fun deleteVerifiedAreaErrorScenarios() = createErrorStream(
            40054 to DeleteVerifiedAreaError.InvalidVerifiedArea::class,
            40032 to DeleteVerifiedAreaError.VerifiedAreaLimitViolation::class,
            40055 to DeleteVerifiedAreaError.PeriodRestrictedDeleteError::class,
            40404 to DeleteVerifiedAreaError.VerifiedAreaNotFound::class
        )
    }

    @BeforeEach
    fun setUp() {
        testScope = TestScope()
        profileRepositoryImpl = ProfileRepositoryLegacyImpl(testScope, profileRemoteDataSourceLegacy, profileInfoCacheLegacy)
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
        coEvery { profileRemoteDataSourceLegacy.validateNickname(any()) } throws fakeRemoteError

        // When
        val result = profileRepositoryImpl.validateNickname("")

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }

    @ParameterizedTest
    @MethodSource("saveSpotErrorScenarios")
    fun `장소 저장 API 실패 시 에러 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<SaveSpotError>
    ) = runTest {
        // Given
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { profileRemoteDataSourceLegacy.saveSpot(any()) } throws fakeRemoteError

        // When
        val result = profileRepositoryImpl.saveSpot(0)

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }

    @ParameterizedTest
    @MethodSource("replaceVerifiedAreaErrorScenarios")
    fun `인증 지역 변경 API 실패 시 에러 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<ReplaceVerifiedArea>
    ) = runTest {
        // Given
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { profileRemoteDataSourceLegacy.replaceVerifiedArea(any(), any(), any()) } throws fakeRemoteError

        // When
        val result = profileRepositoryImpl.replaceVerifiedArea(0, .0, .0)

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }

    @ParameterizedTest
    @MethodSource("deleteVerifiedAreaErrorScenarios")
    fun `인증 지역 삭제 API 실패 시 에러 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<ReplaceVerifiedArea>
    ) = runTest {
        // Given
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { profileRemoteDataSourceLegacy.deleteVerifiedArea(any()) } throws fakeRemoteError

        // When
        val result = profileRepositoryImpl.deleteVerifiedArea(0)

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }
}