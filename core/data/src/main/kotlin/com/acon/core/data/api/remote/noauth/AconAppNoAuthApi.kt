package com.acon.core.data.api.remote.noauth

import com.acon.core.data.dto.response.app.ShouldUpdateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AconAppNoAuthApi {
    @GET("/api/v1/app-updates")
    suspend fun fetchShouldUpdateApp(
        @Query("version") currentVersion: String,
        @Query("platform") platform: String = "android"
    ): ShouldUpdateResponse
}