package com.acon.core.data.api.remote.auth

import com.acon.core.data.dto.request.TastePreferenceRequest
import retrofit2.http.Body
import retrofit2.http.PUT

interface OnboardingAuthApi {

    @PUT("/api/v1/preference")
    suspend fun submitTastePreferenceResult(
        @Body tastePreferenceRequest: TastePreferenceRequest
    )
}