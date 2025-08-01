package com.acon.acon.data.api.remote

import com.acon.acon.data.dto.response.MapSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MapSearchApi {
    @GET("/v1/search/local.json")
    suspend fun fetchMapSearch(
        @Query("query") query: String,
        @Query("display") display: Int = 10,
        @Query("sort") sort: String = "random"
    ): MapSearchResponse
}