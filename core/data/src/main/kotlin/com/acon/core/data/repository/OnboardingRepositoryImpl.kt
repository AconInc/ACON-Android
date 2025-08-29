package com.acon.core.data.repository

import com.acon.acon.core.model.model.OnboardingPreferences
import com.acon.acon.core.model.type.FoodType
import com.acon.acon.domain.error.onboarding.PostTastePreferenceResultError
import com.acon.acon.domain.repository.OnboardingRepository
import com.acon.core.data.datasource.local.OnboardingLocalDataSource
import com.acon.core.data.datasource.remote.OnboardingRemoteDataSource
import com.acon.core.data.dto.request.TastePreferenceRequest
import com.acon.core.data.error.runCatchingWith
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingRemoteDataSource: OnboardingRemoteDataSource,
    private val onboardingLocalDataSource: OnboardingLocalDataSource,
) : OnboardingRepository {

    override suspend fun submitTastePreferenceResult(
        dislikeFoods: List<FoodType>
    ): Result<Unit> {
        return runCatchingWith(PostTastePreferenceResultError()) {
            val request = TastePreferenceRequest(dislikeFoods = dislikeFoods.map { it.name })
            onboardingRemoteDataSource.submitTastePreferenceResult(request)
            onboardingLocalDataSource.updateHasPreference(true)
        }
    }

    override suspend fun updateHasTastePreference(hasPreference: Boolean): Result<Unit> {
        return runCatchingWith {
            onboardingLocalDataSource.updateHasPreference(hasPreference)
        }
    }

    override suspend fun updateShouldShowIntroduce(shouldShow: Boolean): Result<Unit> {
        return runCatchingWith {
            onboardingLocalDataSource.updateShouldShowIntroduce(shouldShow)
        }
    }

    override suspend fun updateHasVerifiedArea(hasVerifiedArea: Boolean): Result<Unit> {
        return runCatchingWith {
            onboardingLocalDataSource.updateHasVerifiedArea(hasVerifiedArea)
        }
    }

    override suspend fun getOnboardingPreferences(): Result<OnboardingPreferences> {
        return runCatchingWith {
            val entity = onboardingLocalDataSource.getOnboardingPreferences()
            OnboardingPreferences(
                shouldShowIntroduce = entity.shouldShowIntroduce,
                hasTastePreference = entity.hasTastePreference,
                hasVerifiedArea = entity.hasVerifiedArea
            )
        }
    }
}