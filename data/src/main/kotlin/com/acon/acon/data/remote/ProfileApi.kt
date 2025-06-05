package com.acon.acon.data.remote

import com.acon.acon.data.dto.request.updateProfileRequest
import com.acon.acon.data.dto.response.profile.PreSignedUrlResponse
import com.acon.acon.data.dto.response.profile.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface ProfileApi {
    @GET("/api/v1/members/me")
    suspend fun fetchProfile(): ProfileResponse

    @GET("/api/v1/images/presigned-url?imageType=PROFILE")
    suspend fun getPreSignedUrl() : PreSignedUrlResponse

    @GET("/api/v1/nickname/validate")
    suspend fun validateNickname(
        @Query("nickname", encoded = true) nickname: String
    ): Response<Unit>

    @PATCH("/api/v1/members/me")
    suspend fun updateProfile(
        @Body request: updateProfileRequest
    ): Response<Unit>

}