package com.acon.acon.data.remote

import com.acon.acon.data.dto.request.RecentNavigationLocationRequest
import com.acon.acon.data.dto.request.SpotListRequest
import com.acon.acon.data.dto.response.SpotDetailInfoResponse
import com.acon.acon.data.dto.response.SpotDetailMenuListResponse
import com.acon.acon.data.dto.response.SpotListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SpotApi {

    @POST("/api/v1/spots")
    suspend fun fetchSpotList(
        @Body request: SpotListRequest
    ): SpotListResponse

    @POST("/api/v1/member/guided-spot")
    suspend fun fetchRecentNavigationLocation(
        @Body request: RecentNavigationLocationRequest
    )

    @GET("/api/v1/spot/{spotId}")
    suspend fun getSpotDetailInfo(
        @Path ("spotId") spotId: Long
    ): SpotDetailInfoResponse

    @GET("/api/v1/spot/{spotId}/menus")
    suspend fun getSpotMenuList(
        @Path ("spotId") spotId: Long
    ): SpotDetailMenuListResponse
}