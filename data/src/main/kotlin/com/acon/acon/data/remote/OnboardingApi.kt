package com.acon.acon.data.remote

import com.acon.acon.data.dto.request.PostOnboardingResultRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface OnboardingApi {

    @PUT("/api/v1/members/preference")
    suspend fun postOnboardingResult(
        @Body body: PostOnboardingResultRequest
    ) : Response<Unit>
}