package com.acon.core.data.api.remote.auth

import com.acon.core.data.dto.request.ReplaceVerifiedAreaRequest
import com.acon.core.data.dto.request.SaveSpotRequest
import com.acon.core.data.dto.request.UpdateProfileRequestLegacy
import com.acon.core.data.dto.response.area.VerifiedAreaListResponse
import com.acon.core.data.dto.response.profile.PreSignedUrlResponse
import com.acon.core.data.dto.response.profile.ProfileResponseLegacy
import com.acon.core.data.dto.response.profile.SavedSpotsResponseLegacy
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileAuthApiLegacy {
    @GET("/api/v1/members/me")
    suspend fun fetchProfile(): ProfileResponseLegacy

    @GET("/api/v1/images/presigned-url?imageType=PROFILE")
    suspend fun getPreSignedUrl() : PreSignedUrlResponse

    @GET("/api/v1/nickname/validate")
    suspend fun validateNickname(
        @Query("nickname", encoded = true) nickname: String
    ): Response<Unit>

    @PATCH("/api/v1/members/me")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequestLegacy
    ): Response<Unit>

    @GET("/api/v1/saved-spots")
    suspend fun fetchSavedSpots(): SavedSpotsResponseLegacy

    @POST("/api/v1/saved-spots")
    suspend fun saveSpot(
        @Body saveSpotRequest: SaveSpotRequest
    )

    @GET("/api/v1/verified-areas")
    suspend fun fetchVerifiedAreaList() : VerifiedAreaListResponse

    @POST("/api/v1/verified-areas/replacement")
    suspend fun replaceVerifiedArea(
        @Body request: ReplaceVerifiedAreaRequest
    )

    @DELETE("/api/v1/verified-areas/{verifiedAreaId}")
    suspend fun deleteVerifiedArea(
        @Path("verifiedAreaId") verifiedAreaId: Long
    )
}