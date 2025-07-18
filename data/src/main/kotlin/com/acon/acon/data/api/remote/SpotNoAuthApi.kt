package com.acon.acon.data.api.remote

import com.acon.acon.data.dto.request.RecentNavigationLocationRequest
import com.acon.acon.data.dto.response.MenuBoardListResponse
import com.acon.acon.data.dto.response.SpotDetailResponse
import com.acon.acon.data.dto.response.area.LegalAreaResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotNoAuthApi {

    @POST("/api/v1/guided-spots")
    suspend fun fetchRecentNavigationLocation(
        @Body request: RecentNavigationLocationRequest
    )

    @GET("api/v1/area")
    suspend fun getLegalDong(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ) : LegalAreaResponse

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