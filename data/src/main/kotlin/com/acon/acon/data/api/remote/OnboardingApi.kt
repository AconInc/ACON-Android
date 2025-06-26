package com.acon.acon.data.api.remote

import com.acon.acon.data.dto.request.OnboardingRequest
import retrofit2.http.Body
import retrofit2.http.PUT

interface OnboardingApi {

    @PUT("/api/v1/preference")
    suspend fun submitOnboardingResult(
        @Body onboardingRequest: OnboardingRequest
    )
}