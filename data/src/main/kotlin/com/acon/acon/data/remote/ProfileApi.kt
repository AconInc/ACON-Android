package com.acon.data.api.remote

import com.acon.data.dto.response.profile.ProfileResponse
import retrofit2.http.GET

interface ProfileApi {

    @GET("/api/v1/members/me")
    suspend fun fetchProfile(): ProfileResponse
}