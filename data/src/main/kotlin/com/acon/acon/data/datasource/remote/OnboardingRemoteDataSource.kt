package com.acon.acon.data.datasource.remote

import com.acon.acon.data.dto.request.PostOnboardingResultRequest
import com.acon.acon.data.remote.OnboardingApi
import retrofit2.Response
import javax.inject.Inject

class OnboardingRemoteDataSource @Inject constructor(
    private val onboardingApi: OnboardingApi
) {

    suspend fun postResult(request: PostOnboardingResultRequest): Response<Unit> {
        return onboardingApi.postOnboardingResult(request)
    }
}