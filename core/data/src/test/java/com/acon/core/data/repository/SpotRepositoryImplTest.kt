package com.acon.core.data.repository

import com.acon.acon.domain.error.spot.AddBookmarkError
import com.acon.acon.domain.error.spot.DeleteBookmarkError
import com.acon.acon.domain.error.spot.FetchMenuBoardsError
import com.acon.acon.domain.error.spot.FetchRecentNavigationLocationError
import com.acon.acon.domain.error.spot.FetchSpotListError
import com.acon.acon.domain.error.spot.GetSpotDetailInfoError
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.core.data.assertValidErrorMapping
import com.acon.core.data.cache.ProfileInfoCache
import com.acon.core.data.createErrorStream
import com.acon.core.data.createFakeRemoteError
import com.acon.core.data.datasource.remote.SpotRemoteDataSource
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.reflect.KClass

@ExtendWith(MockKExtension::class)
class SpotRepositoryImplTest {

    @RelaxedMockK
    lateinit var spotRemoteDataSource: SpotRemoteDataSource

    @RelaxedMockK
    lateinit var profileInfoCache: ProfileInfoCache

    @RelaxedMockK
    lateinit var profileRepository: ProfileRepository

    @InjectMockKs
    lateinit var spotRepositoryImpl: SpotRepositoryImpl

    companion object {
        @JvmStatic
        fun fetchSpotListErrorScenarios() = createErrorStream(
            40015 to FetchSpotListError.InvalidSpotType::class,
            40018 to FetchSpotListError.InvalidCategory::class,
            40019 to FetchSpotListError.InvalidOption::class,
            40020 to FetchSpotListError.NonMatchingSpotTypeAndCategory::class,
            40021 to FetchSpotListError.NonMatchingCategoryAndOption::class,
            40405 to FetchSpotListError.OutOfServiceArea::class
        )
        @JvmStatic
        fun fetchRecentNavigationLocationErrorScenarios() = createErrorStream(
            40403 to FetchRecentNavigationLocationError.SpaceNotFoundError::class
        )
        @JvmStatic
        fun getSpotDetailInfoErrorScenarios() = createErrorStream(
            40403 to GetSpotDetailInfoError.SpaceNotFoundError::class
        )
        @JvmStatic
        fun fetchMenuBoardsErrorScenarios() = createErrorStream(
            40403 to FetchMenuBoardsError.SpotNotFoundError::class
        )
        @JvmStatic
        fun addBookmarkErrorScenarios() = createErrorStream(
            40403 to AddBookmarkError.SpaceNotFoundError::class
        )
        @JvmStatic
        fun deleteBookmarkErrorScenarios() = createErrorStream(
            40403 to DeleteBookmarkError.SpaceNotFoundError::class
        )
    }

    @ParameterizedTest
    @MethodSource("fetchSpotListErrorScenarios")
    fun `장소 리스트 API 실패 시 에러 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<FetchSpotListError>
    ) = runTest {
        // Given
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { spotRemoteDataSource.fetchSpotList(any()) } throws fakeRemoteError

        // When
        val result = spotRepositoryImpl.fetchSpotList(.0, .0, mockk(relaxed = true))

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }

    @ParameterizedTest
    @MethodSource("fetchRecentNavigationLocationErrorScenarios")
    fun `최근 길찾기 장소 API 실패 시 에러 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<FetchRecentNavigationLocationError>
    ) = runTest {
        // Given
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { spotRemoteDataSource.fetchRecentNavigationLocation(any()) } throws fakeRemoteError

        // When
        val result = spotRepositoryImpl.fetchRecentNavigationLocation(0)

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }

    @ParameterizedTest
    @MethodSource("getSpotDetailInfoErrorScenarios")
    fun `장소 디테일 API 실패 시 에러 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<GetSpotDetailInfoError>
    ) = runTest {
        // Given
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { spotRemoteDataSource.fetchSpotDetail(any(), any()) } throws fakeRemoteError

        // When
        val result = spotRepositoryImpl.fetchSpotDetail(0, false)

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }

    @ParameterizedTest
    @MethodSource("fetchMenuBoardsErrorScenarios")
    fun `장소 메뉴판 API 실패 시 에러 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<FetchMenuBoardsError>
    ) = runTest {
        // Given
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { spotRemoteDataSource.fetchMenuBoards(any()) } throws fakeRemoteError

        // When
        val result = spotRepositoryImpl.fetchMenuBoards(0)

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }

    @ParameterizedTest
    @MethodSource("addBookmarkErrorScenarios")
    fun `북마크 추가 API 실패 시 에러 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<AddBookmarkError>
    ) = runTest {
        // Given
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { spotRemoteDataSource.addBookmark(any()) } throws fakeRemoteError

        // When
        val result = spotRepositoryImpl.addBookmark(0)

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }

    @ParameterizedTest
    @MethodSource("deleteBookmarkErrorScenarios")
    fun `북마크 삭제 API 실패 시 에러 객체를 반환한다`(
        errorCode: Int,
        expectedErrorClass: KClass<DeleteBookmarkError>
    ) = runTest {
        // Given
        val fakeRemoteError = createFakeRemoteError(errorCode)
        coEvery { spotRemoteDataSource.deleteBookmark(any()) } throws fakeRemoteError

        // When
        val result = spotRepositoryImpl.deleteBookmark(0)

        // Then
        assertValidErrorMapping(result, expectedErrorClass)
    }
}