package com.acon.core.data.datasource.remote

import com.acon.core.data.dto.request.OnboardingRequest
import com.acon.core.data.api.remote.auth.OnboardingAuthApi
import javax.inject.Inject

class OnboardingRemoteDataSource @Inject constructor(
    private val onboardingAuthApi: OnboardingAuthApi
) {

    suspend fun submitOnboardingResult(request: OnboardingRequest) {
        return onboardingAuthApi.submitOnboardingResult(request)
    }
}