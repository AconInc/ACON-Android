package com.acon.acon.data.repository

import com.acon.acon.data.datasource.remote.SpotRemoteDataSource
import com.acon.acon.data.dto.request.ConditionRequest
import com.acon.acon.data.dto.request.FilterListRequest
import com.acon.acon.data.dto.request.RecentNavigationLocationRequest
import com.acon.acon.data.dto.request.SpotListRequest
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.area.GetLegalDongError
import com.acon.acon.domain.error.spot.FetchRecentNavigationLocationError
import com.acon.acon.domain.error.spot.FetchSpotListError
import com.acon.acon.domain.error.spot.GetSpotDetailInfoError
import com.acon.acon.domain.error.spot.GetSpotMenuListError
import com.acon.acon.domain.model.area.LegalArea
import com.acon.acon.domain.model.spot.Condition
import com.acon.acon.domain.model.spot.Spot
import com.acon.acon.domain.model.spot.SpotDetailInfo
import com.acon.acon.domain.model.spot.SpotDetailMenu
import com.acon.acon.domain.repository.SpotRepository
import javax.inject.Inject

class SpotRepositoryImpl @Inject constructor(
    private val spotRemoteDataSource: SpotRemoteDataSource
) : SpotRepository {

    override suspend fun fetchSpotList(
        latitude: Double,
        longitude: Double,
        condition: Condition,
    ): Result<List<Spot>> {
        return runCatchingWith(*FetchSpotListError.createErrorInstances()) {
            spotRemoteDataSource.fetchSpotList(
                SpotListRequest(
                    latitude = latitude,
                    longitude = longitude,
                    condition = ConditionRequest(
                        spotType = condition.spotType?.name,
                        filterList = condition.filterList?.map { filter ->
                            FilterListRequest(
                                category = filter.category.name,
                                optionList = filter.optionList.map { optionTypes -> optionTypes.getName() }
                            )
                        }, walkingTime = condition.walkingTime, priceRange = condition.priceRange
                    ),
                )
            ).spotList.map { it.toSpot() }
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

    override suspend fun getSpotDetailInfo(
        spotId: Long,
    ): Result<SpotDetailInfo> {
        return runCatchingWith(*GetSpotDetailInfoError.createErrorInstances()) {
            spotRemoteDataSource.getSpotDetailInfo(spotId).toSpotDetailInfo()
        }
    }

    override suspend fun getSpotMenuList(
        spotId: Long
    ): Result<List<SpotDetailMenu>> {
        return runCatchingWith(*GetSpotMenuListError.createErrorInstances()) {
            spotRemoteDataSource.getSpotMenuList(spotId).menuList.map { it.toSpotDetailMenu() }
        }
    }

    override suspend fun getLegalDong(latitude: Double, longitude: Double): Result<LegalArea> {
        return runCatchingWith(*GetLegalDongError.createErrorInstances()) {
            spotRemoteDataSource.getLegalDong(latitude, longitude).toLegalArea()
        }
    }

}