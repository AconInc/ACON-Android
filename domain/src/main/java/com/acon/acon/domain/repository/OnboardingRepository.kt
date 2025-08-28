package com.acon.acon.domain.repository

import com.acon.acon.core.model.type.FoodType

interface OnboardingRepository {
    suspend fun submitOnboardingResult(
        dislikeFoodList: List<FoodType>
    ): Result<Unit>

    suspend fun saveDidOnboarding(didOnboarding: Boolean): Result<Unit>
    suspend fun getDidOnboarding(): Result<Boolean>
}
