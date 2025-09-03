package com.acon.core.data.repository

import com.acon.acon.core.model.model.profile.SavedSpotLegacy
import com.acon.acon.core.model.model.spot.Condition
import com.acon.acon.core.model.model.spot.MenuBoardList
import com.acon.acon.core.model.model.spot.SpotDetail
import com.acon.acon.core.model.model.spot.SpotList
import com.acon.core.data.cache.ProfileInfoCacheLegacy
import com.acon.core.data.datasource.remote.SpotRemoteDataSource
import com.acon.core.data.dto.request.AddBookmarkRequest
import com.acon.core.data.dto.request.ConditionRequest
import com.acon.core.data.dto.request.FilterListRequest
import com.acon.core.data.dto.request.RecentNavigationLocationRequest
import com.acon.core.data.dto.request.SpotListRequest
import com.acon.core.data.error.runCatchingWith
import com.acon.core.data.session.SessionHandler
import com.acon.acon.domain.error.spot.AddBookmarkError
import com.acon.acon.domain.error.spot.DeleteBookmarkError
import com.acon.acon.domain.error.spot.FetchMenuBoardsError
import com.acon.acon.domain.error.spot.FetchRecentNavigationLocationError
import com.acon.acon.domain.error.spot.FetchSpotListError
import com.acon.acon.domain.error.spot.GetSpotDetailInfoError
import com.acon.acon.domain.repository.ProfileRepositoryLegacy
import com.acon.acon.domain.repository.SpotRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SpotRepositoryImpl @Inject constructor(
    private val spotRemoteDataSource: SpotRemoteDataSource,
    private val profileInfoCacheLegacy: ProfileInfoCacheLegacy,
    private val profileRepositoryLegacy: ProfileRepositoryLegacy,
    private val sessionHandler: SessionHandler
) : SpotRepository {

    override suspend fun fetchSpotList(
        latitude: Double,
        longitude: Double,
        condition: Condition,
    ): Result<SpotList> {
        return runCatchingWith(FetchSpotListError()) {
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
                ), sessionHandler.getUserType().first()
            ).toSpotList()
        }
    }

    override suspend fun fetchRecentNavigationLocation(
        spotId: Long,
    ): Result<Unit> {
        return runCatchingWith(FetchRecentNavigationLocationError()) {
            spotRemoteDataSource.fetchRecentNavigationLocation(
                RecentNavigationLocationRequest(spotId = spotId)
            )
        }
    }

    override suspend fun fetchSpotDetail(
        spotId: Long,
        isDeepLink: Boolean
    ): Result<SpotDetail> {
        return runCatchingWith(GetSpotDetailInfoError()) {
            spotRemoteDataSource.fetchSpotDetail(spotId, isDeepLink).toSpotDetail()
        }
    }

    override suspend fun fetchMenuBoards(
        spotId: Long
    ): Result<MenuBoardList> {
        return runCatchingWith(FetchMenuBoardsError()) {
            spotRemoteDataSource.fetchMenuBoards(spotId).toMenuBoardList()
        }
    }

    override suspend fun fetchSpotDetailFromUser(spotId: Long): Result<SpotDetail> {
        return runCatchingWith(GetSpotDetailInfoError()) {
            spotRemoteDataSource.fetchSpotDetailFromUser(spotId).toSpotDetail()
        }
    }

    override suspend fun fetchSavedSpotList(): Result<List<SavedSpotLegacy>> {
        return runCatchingWith() {
            spotRemoteDataSource.fetchSavedSpotList().savedSpotResponseLegacyList?.map {
                it.toSavedSpot()
            }.orEmpty()
        }
    }

    override suspend fun addBookmark(spotId: Long): Result<Unit> {
        return runCatchingWith(AddBookmarkError()) {
            spotRemoteDataSource.addBookmark(AddBookmarkRequest(spotId))

            profileRepositoryLegacy.fetchSavedSpots().onSuccess { fetched ->
                (profileInfoCacheLegacy.data.value.getOrNull()
                    ?: return@onSuccess).let { profileInfo ->
                    profileInfoCacheLegacy.updateData(profileInfo.copy(savedSpotLegacies = fetched))
                }
            }
        }
    }

    override suspend fun deleteBookmark(spotId: Long): Result<Unit> {
        return runCatchingWith(DeleteBookmarkError()) {
            spotRemoteDataSource.deleteBookmark(spotId)

            profileRepositoryLegacy.fetchSavedSpots().onSuccess { fetched ->
                (profileInfoCacheLegacy.data.value.getOrNull()
                    ?: return@onSuccess).let { profileInfo ->
                    profileInfoCacheLegacy.updateData(profileInfo.copy(savedSpotLegacies = fetched))
                }
            }
        }
    }
}