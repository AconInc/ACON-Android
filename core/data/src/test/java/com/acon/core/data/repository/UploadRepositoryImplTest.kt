package com.acon.core.data.repository

import com.acon.acon.domain.error.upload.GetVerifySpotLocationError
import com.acon.acon.domain.error.upload.UploadReviewError
import com.acon.acon.domain.error.user.GetSuggestionsError
import com.acon.core.data.assertValidErrorMapping
import com.acon.core.data.createErrorStream
import com.acon.core.data.createFakeRemoteError
import com.acon.core.data.datasource.remote.UploadRemoteDataSource
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
class UploadRepositoryImplTest {

    @RelaxedMockK
    lateinit var uploadRemoteDataSource: UploadRemoteDataSource

    @InjectMockKs
    lateinit var uploadRepositoryImpl: UploadRepositoryImpl

    companion object {
        @JvmStatic
        fun getSuggestionsErrorScenarios() = createErrorStream(
            40405 to GetSuggestionsError.OutOfServiceAreaError::class
        )
        @JvmStatic
        fun getVerifySpotLocationErrorScenarios() = createErrorStream(
            40403 to GetVerifySpotLocationError.NotExistSpot::class,
            40405 to GetVerifySpotLocationError.OutOfServiceAreaError::class
        )
        @JvmStatic
        fun uploadReviewErrorScenarios() = createErrorStream(
            40402 to UploadReviewError.NotExistUser::class,
            40098 to UploadReviewError.NotEnoughAcorn::class
        )
    }

    @ParameterizedTest
    @MethodSource("getSuggestionsErrorScenarios")
    fun `업로드 장소 추천 API 실패 시 에러 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<GetSuggestionsError>
    ) = runTest {
        // Given
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { uploadRemoteDataSource.getSuggestions(any(), any()) } throws fakeRemoteError

        // When
        val result = uploadRepositoryImpl.getSuggestions(.0, .0)

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }

    @ParameterizedTest
    @MethodSource("getVerifySpotLocationErrorScenarios")
    fun `업로드 장소 좌표 검사 API 실패 시 에러 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<GetVerifySpotLocationError>
    ) = runTest {
        // Given
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { uploadRemoteDataSource.verifyLocation(any(), any(), any()) } throws fakeRemoteError

        // When
        val result = uploadRepositoryImpl.verifyLocation(0, .0, .0)

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }

    @ParameterizedTest
    @MethodSource("uploadReviewErrorScenarios")
    fun `리뷰 남기기 API 실패 시 에러 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<UploadReviewError>
    ) = runTest {
        // Given
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { uploadRemoteDataSource.submitReview(any(), any()) } throws fakeRemoteError

        // When
        val result = uploadRepositoryImpl.submitReview(0, 0)

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }
}