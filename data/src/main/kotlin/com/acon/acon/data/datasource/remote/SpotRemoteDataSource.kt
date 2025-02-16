package com.acon.acon.data.datasource.remote

import com.acon.acon.data.dto.request.RecentNavigationLocationRequest
import com.acon.acon.data.dto.request.SpotListRequest
import com.acon.acon.data.dto.response.SpotDetailInfoResponse
import com.acon.acon.data.dto.response.SpotDetailMenuListResponse
import com.acon.acon.data.dto.response.SpotListResponse
import com.acon.acon.data.remote.SpotApi
import javax.inject.Inject

class SpotRemoteDataSource @Inject constructor(
    private val spotApi: SpotApi
) {

    suspend fun fetchSpotList(request: SpotListRequest): SpotListResponse {
        return spotApi.fetchSpotList(request)
    }

    suspend fun fetchRecentNavigationLocation(request: RecentNavigationLocationRequest) {
        return spotApi.fetchRecentNavigationLocation(request)
    }

    suspend fun getSpotDetailInfo(spotId: Long): SpotDetailInfoResponse {
        return spotApi.getSpotDetailInfo(spotId)
    }

    suspend fun getSpotMenuList(spotId: Long): SpotDetailMenuListResponse {
        return spotApi.getSpotMenuList(spotId)
    }
}