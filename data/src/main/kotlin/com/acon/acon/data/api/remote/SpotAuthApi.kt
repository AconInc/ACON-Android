package com.acon.acon.data.api.remote

import com.acon.acon.data.dto.request.SpotListRequest
import com.acon.acon.data.dto.response.SpotDetailResponse
import com.acon.acon.data.dto.response.SpotListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SpotAuthApi {

    @POST("/api/v1/spots")
    suspend fun fetchSpotList(
        @Body request: SpotListRequest
    ): SpotListResponse

    @GET("/api/v1/spots/{spotId}")
    suspend fun fetchSpotDetail(
        @Path("spotId") spotId: Long
    ): SpotDetailResponse
}