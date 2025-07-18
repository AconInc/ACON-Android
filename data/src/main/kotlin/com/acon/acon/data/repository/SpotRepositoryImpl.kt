package com.acon.acon.data.repository

import com.acon.acon.data.cache.ProfileInfoCache
import com.acon.acon.data.datasource.remote.SpotRemoteDataSource
import com.acon.acon.data.dto.request.AddBookmarkRequest
import com.acon.acon.data.dto.request.ConditionRequest
import com.acon.acon.data.dto.request.FilterListRequest
import com.acon.acon.data.dto.request.RecentNavigationLocationRequest
import com.acon.acon.data.dto.request.SpotListRequest
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.area.GetLegalDongError
import com.acon.acon.domain.error.spot.AddBookmarkError
import com.acon.acon.domain.error.spot.DeleteBookmarkError
import com.acon.acon.domain.error.spot.FetchMenuBoardsError
import com.acon.acon.domain.error.spot.FetchRecentNavigationLocationError
import com.acon.acon.domain.error.spot.FetchSpotListError
import com.acon.acon.domain.error.spot.GetSpotDetailInfoError
import com.acon.acon.domain.model.area.LegalArea
import com.acon.acon.domain.model.profile.SavedSpot
import com.acon.acon.domain.model.spot.Condition
import com.acon.acon.domain.model.spot.MenuBoardList
import com.acon.acon.domain.model.spot.SpotDetail
import com.acon.acon.domain.model.spot.v2.SpotList
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.acon.domain.repository.SpotRepository
import javax.inject.Inject

class SpotRepositoryImpl @Inject constructor(
    private val spotRemoteDataSource: SpotRemoteDataSource,
    private val profileInfoCache: ProfileInfoCache,
    private val profileRepository: ProfileRepository
) : SpotRepository {

    override suspend fun fetchSpotList(
        latitude: Double,
        longitude: Double,
        condition: Condition,
    ): Result<SpotList> {
        return runCatchingWith(*FetchSpotListError.createErrorInstances()) {
            spotRemoteDataSource.fetchSpotList(
                SpotListRequest(
                    latitude = latitude,
                    longitude = longitude,
                    condition = ConditionRequest(
                        spotType = condition.spotType.name,
                        filterList = condition.filterList?.map { filter ->
                            FilterListRequest(
                                category = filter.category.name,
                                optionList = filter.optionList.map { optionTypes -> optionTypes.getName() }
                            )
                        }
                    ),
                )
            ).toSpotList()
        }
    }

    override suspend fun fetchRecentNavigationLocation(
        spotId: Long,
    ): Result<Unit> {
        return runCatchingWith(*FetchRecentNavigationLocationError.createErrorInstances()) {
            spotRemoteDataSource.fetchRecentNavigationLocation(
                RecentNavigationLocationRequest(spotId = spotId)
            )
        }
    }

    override suspend fun fetchSpotDetail(
        spotId: Long,
        isDeepLink: Boolean
    ): Result<SpotDetail> {
        return runCatchingWith(*GetSpotDetailInfoError.createErrorInstances()) {
            spotRemoteDataSource.fetchSpotDetail(spotId, isDeepLink).toSpotDetail()
        }
    }

    override suspend fun getLegalDong(latitude: Double, longitude: Double): Result<LegalArea> {
        return runCatchingWith(*GetLegalDongError.createErrorInstances()) {
            spotRemoteDataSource.getLegalDong(latitude, longitude).toLegalArea()
        }
    }

    override suspend fun fetchMenuBoards(
        spotId: Long
    ): Result<MenuBoardList> {
        return runCatchingWith(*FetchMenuBoardsError.createErrorInstances()) {
            spotRemoteDataSource.fetchMenuBoards(spotId).toMenuBoardList()
        }
    }

    override suspend fun fetchSpotDetailFromUser(spotId: Long): Result<SpotDetail> {
        return runCatchingWith(*GetSpotDetailInfoError.createErrorInstances()) {
            spotRemoteDataSource.fetchSpotDetailFromUser(spotId).toSpotDetail()
        }
    }

    override suspend fun fetchSavedSpotList(): Result<List<SavedSpot>> {
        return runCatchingWith() {
            spotRemoteDataSource.fetchSavedSpotList().savedSpotResponseList?.map {
                it.toSavedSpot()
            }.orEmpty()
        }
    }

    override suspend fun addBookmark(spotId: Long): Result<Unit> {
        return runCatchingWith(*AddBookmarkError.createErrorInstances()) {
            spotRemoteDataSource.addBookmark(AddBookmarkRequest(spotId))

            profileRepository.fetchSavedSpots().onSuccess { fetched ->
                (profileInfoCache.data.value.getOrNull()
                    ?: return@onSuccess).let { profileInfo ->
                    profileInfoCache.updateData(profileInfo.copy(savedSpots = fetched))
                }
            }
        }
    }

    override suspend fun deleteBookmark(spotId: Long): Result<Unit> {
        return runCatchingWith(*DeleteBookmarkError.createErrorInstances()) {
            spotRemoteDataSource.deleteBookmark(spotId)

            profileRepository.fetchSavedSpots().onSuccess { fetched ->
                (profileInfoCache.data.value.getOrNull()
                    ?: return@onSuccess).let { profileInfo ->
                    profileInfoCache.updateData(profileInfo.copy(savedSpots = fetched))
                }
            }
        }
    }
}