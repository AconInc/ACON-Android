package com.acon.acon.data.remote

import com.acon.acon.data.dto.request.AreaVerificationRequest
import com.acon.acon.data.dto.response.AreaVerificationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AreaVerificationApi {
    @POST("/api/v1/members/verified-areas")
    suspend fun verifyArea(
        @Body request: AreaVerificationRequest
    ): AreaVerificationResponse
}
