package com.acon.core.data.repository

import com.acon.acon.domain.error.onboarding.PostTastePreferenceResultError
import com.acon.core.data.assertValidErrorMapping
import com.acon.core.data.createErrorStream
import com.acon.core.data.createFakeRemoteError
import com.acon.core.data.datasource.remote.OnboardingRemoteDataSource
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.reflect.KClass

@ExtendWith(MockKExtension::class)
class OnboardingRepositoryImplTest {

    @RelaxedMockK
    lateinit var onboardingRemoteDataSource: OnboardingRemoteDataSource

    @InjectMockKs
    lateinit var onboardingRepository: OnboardingRepositoryImpl

    companion object {
        @JvmStatic
        fun postOnboardingResultErrorScenarios() = createErrorStream(
            40013 to PostTastePreferenceResultError.InvalidDislikeFood::class
        )
    }

    @ParameterizedTest
    @MethodSource("postOnboardingResultErrorScenarios")
    fun `싫어하는 음식 API 실패 시 에러 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<PostTastePreferenceResultError>
    ) = runTest {
        // Given
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { onboardingRemoteDataSource.submitTastePreferenceResult(any()) } throws fakeRemoteError

        // When
        val result = onboardingRepository.submitTastePreferenceResult(listOf())

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }
}