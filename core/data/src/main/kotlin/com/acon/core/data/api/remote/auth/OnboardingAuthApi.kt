package com.acon.core.data.api.remote.auth

import com.acon.core.data.dto.request.VerifyAreaRequest
import com.acon.core.data.dto.request.TastePreferenceRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface OnboardingAuthApi {

    @PUT("/api/v1/preference")
    suspend fun submitTastePreferenceResult(
        @Body tastePreferenceRequest: TastePreferenceRequest
    )

    @POST("/api/v1/verified-areas")
    suspend fun verifyArea(
        @Body request: VerifyAreaRequest
    )
}