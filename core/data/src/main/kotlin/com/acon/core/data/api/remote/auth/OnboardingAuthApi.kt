package com.acon.core.data.api.remote.auth

import com.acon.core.data.dto.request.OnboardingRequest
import retrofit2.http.Body
import retrofit2.http.PUT

interface OnboardingAuthApi {

    @PUT("/api/v1/preference")
    suspend fun submitOnboardingResult(
        @Body onboardingRequest: OnboardingRequest
    )
}