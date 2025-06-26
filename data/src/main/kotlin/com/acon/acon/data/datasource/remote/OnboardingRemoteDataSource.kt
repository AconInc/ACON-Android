package com.acon.acon.data.datasource.remote

import com.acon.acon.data.dto.request.OnboardingRequest
import com.acon.acon.data.api.remote.OnboardingApi
import javax.inject.Inject

class OnboardingRemoteDataSource @Inject constructor(
    private val onboardingApi: OnboardingApi
) {

    suspend fun submitOnboardingResult(request: OnboardingRequest) {
        return onboardingApi.submitOnboardingResult(request)
    }
}