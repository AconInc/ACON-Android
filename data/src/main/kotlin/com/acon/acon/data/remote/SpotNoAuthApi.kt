package com.acon.acon.data.remote

import com.acon.acon.data.dto.request.RecentNavigationLocationRequest
import com.acon.acon.data.dto.response.SpotDetailInfoResponse
import com.acon.acon.data.dto.response.SpotDetailMenuListResponse
import com.acon.acon.data.dto.response.area.LegalAreaResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotNoAuthApi {

    @POST("/api/v1/members/guided-spots")
    suspend fun fetchRecentNavigationLocation(
        @Body request: RecentNavigationLocationRequest
    )

    @GET("/api/v1/spots/{spotId}")
    suspend fun getSpotDetailInfo(
        @Path ("spotId") spotId: Long
    ): SpotDetailInfoResponse

    @GET("/api/v1/spots/{spotId}/menus")
    suspend fun getSpotMenuList(
        @Path ("spotId") spotId: Long
    ): SpotDetailMenuListResponse

    @GET("api/v1/area")
    suspend fun getLegalDong(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ) : LegalAreaResponse
}