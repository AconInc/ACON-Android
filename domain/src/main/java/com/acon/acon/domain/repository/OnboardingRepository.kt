package com.acon.acon.domain.repository

import com.acon.acon.core.model.type.FoodType

interface OnboardingRepository {
    suspend fun submitOnboardingResult(
        dislikeFoodList: List<com.acon.acon.core.model.type.FoodType>
    ): Result<Unit>
}
