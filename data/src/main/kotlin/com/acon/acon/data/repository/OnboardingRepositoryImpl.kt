package com.acon.acon.data.repository

import com.acon.acon.data.datasource.remote.OnboardingRemoteDataSource
import com.acon.acon.data.dto.request.OnboardingRequest
import com.acon.acon.data.error.runCatchingWith
import com.acon.acon.domain.error.onboarding.PostOnboardingResultError
import com.acon.acon.domain.repository.OnboardingRepository
import com.acon.core.type.FoodType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingRemoteDataSource: OnboardingRemoteDataSource,
) : OnboardingRepository {

    override suspend fun submitOnboardingResult(
        dislikeFoodList: List<FoodType>
    ): Result<Unit> {
        return runCatchingWith(*PostOnboardingResultError.createErrorInstances()) {
            val request = OnboardingRequest(dislikeFoods = dislikeFoodList.map { it.name })
            onboardingRemoteDataSource.submitOnboardingResult(request)
        }
    }
}