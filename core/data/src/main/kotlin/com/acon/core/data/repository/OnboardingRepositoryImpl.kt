package com.acon.core.data.repository

import com.acon.core.data.datasource.remote.OnboardingRemoteDataSource
import com.acon.core.data.dto.request.OnboardingRequest
import com.acon.core.data.error.runCatchingWith
import com.acon.acon.domain.error.onboarding.PostOnboardingResultError
import com.acon.acon.domain.repository.OnboardingRepository
import com.acon.acon.core.model.type.FoodType
import com.acon.core.data.datasource.local.OnboardingLocalDataSource
import com.acon.acon.domain.repository.UserRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingRemoteDataSource: OnboardingRemoteDataSource,
    private val onboardingLocalDataSource: OnboardingLocalDataSource,
    private val userRepository: UserRepository
) : OnboardingRepository {

    override suspend fun submitOnboardingResult(
        dislikeFoodList: List<FoodType>
    ): Result<Unit> {
        return runCatchingWith(PostOnboardingResultError()) {
            val request = OnboardingRequest(dislikeFoods = dislikeFoodList.map { it.name })
            onboardingRemoteDataSource.submitOnboardingResult(request)
            userRepository.saveDidOnboarding(true)
        }
    }

    override suspend fun saveDidOnboarding(didOnboarding: Boolean): Result<Unit> {
        return runCatchingWith {
            onboardingLocalDataSource.saveDidOnboarding(didOnboarding)
        }
    }

    override suspend fun getDidOnboarding(): Result<Boolean> {
        return runCatchingWith {
            onboardingLocalDataSource.getDidOnboarding()
        }
    }
}