package com.acon.core.data.datasource.remote

import com.acon.core.data.dto.request.TastePreferenceRequest
import com.acon.core.data.api.remote.auth.OnboardingAuthApi
import com.acon.core.data.dto.request.VerifyAreaRequest
import javax.inject.Inject

class OnboardingRemoteDataSource @Inject constructor(
    private val onboardingAuthApi: OnboardingAuthApi
) {

    suspend fun submitTastePreferenceResult(request: TastePreferenceRequest) {
        return onboardingAuthApi.submitTastePreferenceResult(request)
    }

    suspend fun verifyArea(
        latitude: Double,
        longitude: Double
    ) {
        return onboardingAuthApi.verifyArea(
            request = VerifyAreaRequest(
                latitude = latitude,
                longitude = longitude
            )
        )
    }
}