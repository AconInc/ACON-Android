package com.acon.core.data.api.remote.noauth

import com.acon.core.data.dto.request.RecentNavigationLocationRequest
import com.acon.core.data.dto.request.SpotListRequest
import com.acon.core.data.dto.response.MenuBoardListResponse
import com.acon.core.data.dto.response.SpotDetailResponse
import com.acon.core.data.dto.response.SpotListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotNoAuthApi {

    @POST("/api/v1/spots")
    suspend fun fetchSpotList(
        @Body request: SpotListRequest
    ): SpotListResponse

    @POST("/api/v1/guided-spots")
    suspend fun fetchRecentNavigationLocation(
        @Body request: RecentNavigationLocationRequest
    )

    @GET("/api/v1/spots/{spotId}")
    suspend fun fetchSpotDetail(
        @Path("spotId") spotId: Long,
        @Query("isDeepLink") isDeepLink: Boolean
    ): SpotDetailResponse

    @GET("/api/v1/spots/{spotId}/menuboards")
    suspend fun fetchMenuBoards(
        @Path ("spotId") spotId: Long
    ) : MenuBoardListResponse
}