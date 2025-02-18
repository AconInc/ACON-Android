package com.acon.acon.data.remote

import com.acon.acon.data.dto.response.profile.PreSignedUrlResponse
import com.acon.acon.data.dto.response.profile.ProfileResponse
import retrofit2.http.GET

interface ProfileApi {
    @GET("/api/v1/members/me")
    suspend fun fetchProfile(): ProfileResponse

    @GET("/api/v1/images/presigned-url?imageType=PROFILE")
    suspend fun getPreSignedUrl() : PreSignedUrlResponse
}