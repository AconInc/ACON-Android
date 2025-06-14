package com.acon.acon.data.api.remote

import com.acon.acon.data.dto.request.SpotListRequest
import com.acon.acon.data.dto.response.SavedSpotListResponse
import com.acon.acon.data.dto.response.SpotListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SpotAuthApi {

    @POST("/api/v1/spots")
    suspend fun fetchSpotList(
        @Body request: SpotListRequest
    ): SpotListResponse

    @GET("/api/v1/saved-spots")
    suspend fun fetchSavedSpotList(): SavedSpotListResponse
}