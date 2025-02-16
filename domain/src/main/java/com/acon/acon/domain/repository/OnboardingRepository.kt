package com.acon.acon.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface OnboardingRepository {

    suspend fun postOnboardingResult(
        dislikeFoodList: Set<String>,
        favoriteCuisineRank: List<String>,
        favoriteSpotType: String,
        favoriteSpotStyle: String,
        favoriteSpotRank: List<String>
    ): Result<Unit>

    val onboardingResultStateFlow: StateFlow<Result<Unit>?>
}
