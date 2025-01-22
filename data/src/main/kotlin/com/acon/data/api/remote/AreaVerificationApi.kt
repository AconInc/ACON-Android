package com.acon.data.api.remote

import com.acon.data.dto.request.AreaVerificationRequest
import com.acon.data.dto.response.AreaVerificationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AreaVerificationApi {
    @POST("/api/v1/member/area")
    suspend fun verifyArea(
        @Body request: AreaVerificationRequest
    ): AreaVerificationResponse
}
