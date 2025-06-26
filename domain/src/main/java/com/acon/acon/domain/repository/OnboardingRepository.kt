package com.acon.acon.domain.repository

import com.acon.core.type.FoodType

interface OnboardingRepository {
    suspend fun submitOnboardingResult(
        dislikeFoodList: List<FoodType>
    ): Result<Unit>
}
