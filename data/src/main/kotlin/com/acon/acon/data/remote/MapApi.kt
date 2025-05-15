package com.acon.acon.data.remote

import com.acon.acon.data.dto.response.DirectionsResponse
import com.acon.acon.data.dto.response.ReverseGeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MapApi {
    @GET("/map-reversegeocode/v2/gc")
    suspend fun fetchReverseGeocoding(
        @Query("coords") query: String,
        @Query("orders") orders: String = "legalcode",
        @Query("output") output: String = "json"
    ): ReverseGeocodingResponse

    @GET("/map-direction/v1/driving")
    suspend fun fetchDirections(
        @Header("x-ncp-apigw-api-key-id") apiKeyID: String = "키",
        @Header("x-ncp-apigw-api-key") apiKeySecret: String = "헤더",
        @Query("start") start: String,
        @Query("goal") goal: String,
        @Query("option") option: String = "trafast",
        @Query("lang") lang: String = "ko"
    ) : DirectionsResponse
}