package com.acon.core.data.datasource.remote

import com.acon.acon.core.model.type.UserType
import com.acon.core.data.api.remote.auth.SpotAuthApi
import com.acon.core.data.api.remote.noauth.SpotNoAuthApi
import com.acon.core.data.dto.request.AddBookmarkRequest
import com.acon.core.data.dto.request.RecentNavigationLocationRequest
import com.acon.core.data.dto.request.SpotListRequest
import com.acon.core.data.dto.response.MenuBoardListResponse
import com.acon.core.data.dto.response.SpotDetailResponse
import com.acon.core.data.dto.response.SpotListResponse
import com.acon.core.data.dto.response.profile.SavedSpotsResponse
import javax.inject.Inject

class SpotRemoteDataSource @Inject constructor(
    private val spotNoAuthApi: SpotNoAuthApi,
    private val spotAuthApi: SpotAuthApi
) {

    suspend fun fetchSpotList(request: SpotListRequest, userType: UserType): SpotListResponse {
        return if (userType == UserType.GUEST)
            spotNoAuthApi.fetchSpotList(request)
        else spotAuthApi.fetchSpotList(request)
    }

    suspend fun fetchRecentNavigationLocation(request: RecentNavigationLocationRequest) {
        return spotNoAuthApi.fetchRecentNavigationLocation(request)
    }

    suspend fun fetchSpotDetail(spotId: Long, isDeepLink: Boolean): SpotDetailResponse {
        return spotNoAuthApi.fetchSpotDetail(spotId, isDeepLink)
    }

    suspend fun fetchMenuBoards(spotId: Long): MenuBoardListResponse {
        return spotNoAuthApi.fetchMenuBoards(spotId)
    }

    suspend fun fetchSpotDetailFromUser(spotId: Long): SpotDetailResponse {
        return spotAuthApi.fetchSpotDetailFromUser(spotId)
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