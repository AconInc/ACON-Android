package com.acon.acon.data.datasource.remote

import com.acon.acon.data.api.remote.SpotAuthApi
import com.acon.acon.data.api.remote.SpotNoAuthApi
import com.acon.acon.data.dto.request.AddBookmarkRequest
import com.acon.acon.data.dto.request.RecentNavigationLocationRequest
import com.acon.acon.data.dto.request.SpotListRequest
import com.acon.acon.data.dto.response.MenuBoardListResponse
import com.acon.acon.data.dto.response.SpotDetailResponse
import com.acon.acon.data.dto.response.SpotListResponse
import com.acon.acon.data.dto.response.area.LegalAreaResponse
import com.acon.acon.data.dto.response.profile.SavedSpotsResponse
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

    suspend fun fetchSpotDetail(spotId: Long, isDeepLink: Boolean): SpotDetailResponse {
        return spotNoAuthApi.fetchSpotDetail(spotId, isDeepLink)
    }

    suspend fun getLegalDong(latitude: Double, longitude: Double): LegalAreaResponse {
        return spotNoAuthApi.getLegalDong(latitude, longitude)
    }

    suspend fun fetchMenuBoards(spotId: Long): MenuBoardListResponse {
        return spotNoAuthApi.fetchMenuBoards(spotId)
    }

    suspend fun fetchSavedSpotList(): SavedSpotsResponse {
        return spotAuthApi.fetchSavedSpotList()
    }

    suspend fun addBookmark(request: AddBookmarkRequest) {
        return spotAuthApi.addBookmark(request)
    }

    suspend fun deleteBookmark(spotId: Long) {
        return spotAuthApi.deleteBookmark(spotId)
    }
}