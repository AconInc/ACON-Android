package com.acon.acon.domain.repository

import com.acon.acon.core.model.model.OnboardingPreferences
import com.acon.acon.core.model.type.FoodType

interface OnboardingRepository {
    suspend fun submitTastePreferenceResult(
        dislikeFoods: List<FoodType>
    ): Result<Unit>

    suspend fun updateHasTastePreference(hasPreference: Boolean): Result<Unit>
    suspend fun updateShouldShowIntroduce(shouldShow: Boolean): Result<Unit>
    suspend fun updateHasVerifiedArea(hasVerifiedArea: Boolean): Result<Unit>

    suspend fun getOnboardingPreferences(): Result<OnboardingPreferences>
}
