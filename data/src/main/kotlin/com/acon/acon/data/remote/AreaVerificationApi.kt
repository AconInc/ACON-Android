package com.acon.acon.data.remote

import com.acon.acon.data.dto.request.AreaVerificationRequest
import com.acon.acon.data.dto.response.area.AreaVerificationResponse
import com.acon.acon.data.dto.response.area.VerifiedAreaListResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AreaVerificationApi {
    @POST("/api/v1/verified-areas")
    suspend fun verifyArea(
        @Body request: AreaVerificationRequest
    ): AreaVerificationResponse

    @GET("/api/v1/verified-areas")
    suspend fun fetchVerifiedAreaList() : VerifiedAreaListResponse

    @DELETE("/api/v1/verified-areas/{verifiedAreaId}")
    suspend fun deleteVerifiedArea(
        @Path("verifiedAreaId") verifiedAreaId: Long
    )
}
