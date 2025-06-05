package com.acon.acon.data.api.remote

import com.acon.acon.data.dto.response.app.ShouldUpdateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AconAppApi {
    @GET("/api/v1/app-updates")
    suspend fun fetchShouldUpdateApp(
        @Query("version") currentVersion: String,
        @Query("platform") platform: String = "android"
    ): ShouldUpdateResponse
}