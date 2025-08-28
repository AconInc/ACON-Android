package com.acon.core.data.api.remote

import com.acon.core.data.dto.response.ReverseGeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MapApi {
    @GET("/map-reversegeocode/v2/gc")
    suspend fun fetchReverseGeocoding(
        @Query("coords") query: String,
        @Query("orders") orders: String = "legalcode",
        @Query("output") output: String = "json"
    ): ReverseGeocodingResponse
}