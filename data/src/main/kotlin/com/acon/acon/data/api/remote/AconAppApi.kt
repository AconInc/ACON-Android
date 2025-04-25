package com.acon.acon.data.api.remote

import retrofit2.http.GET

interface AconAppApi {
    @GET("/api/v1/app/version") // TODO: API 업데이트
    suspend fun fetchShouldUpdateApp(): Boolean
}