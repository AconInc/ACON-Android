package com.acon.core.data.api.remote

import com.acon.core.data.dto.response.MapSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MapSearchApi {
    @GET("/v1/search/local.json")
    suspend fun fetchMapSearch(
        @Query("query") query: String,
        @Query("display") display: Int = 5,
        @Query("sort") sort: String = "random"
    ): MapSearchResponse
}