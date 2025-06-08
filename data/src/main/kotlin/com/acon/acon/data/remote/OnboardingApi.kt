package com.acon.acon.data.remote

import com.acon.acon.data.dto.request.OnboardingRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface OnboardingApi {

    @PUT("/api/v1/preference")
    suspend fun submitOnboardingResult(
        @Body onboardingRequest: OnboardingRequest
    )
}