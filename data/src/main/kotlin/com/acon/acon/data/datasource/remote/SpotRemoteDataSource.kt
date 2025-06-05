package com.acon.acon.data.datasource.remote

import com.acon.acon.data.dto.request.RecentNavigationLocationRequest
import com.acon.acon.data.dto.request.SpotListRequest
import com.acon.acon.data.dto.response.SpotDetailInfoResponse
import com.acon.acon.data.dto.response.SpotDetailMenuListResponse
import com.acon.acon.data.dto.response.SpotListResponse
import com.acon.acon.data.dto.response.area.LegalAreaResponse
import com.acon.acon.data.api.remote.SpotNoAuthApi
import com.acon.acon.data.api.remote.SpotAuthApi
import javax.inject.Inject

class SpotRemoteDataSource @Inject constructor(
    private val spotNoAuthApi: SpotNoAuthApi,
    private val spotAuthApi: SpotAuthApi
) {

    suspend fun fetchSpotList(request: SpotListRequest): SpotListResponse {
        return spotAuthApi.fetchSpotList(request)
    }

    suspend fun fetchRecentNavigationLocation(request: RecentNavigationLocationRequest) {
        return spotNoAuthApi.fetchRecentNavigationLocation(request)
    }

    suspend fun getSpotDetailInfo(spotId: Long): SpotDetailInfoResponse {
        return spotNoAuthApi.getSpotDetailInfo(spotId)
    }

    suspend fun getSpotMenuList(spotId: Long): SpotDetailMenuListResponse {
        return spotNoAuthApi.getSpotMenuList(spotId)
    }

    suspend fun getLegalDong(latitude: Double, longitude: Double): LegalAreaResponse {
        return spotNoAuthApi.getLegalDong(latitude, longitude)
    }
}