package com.acon.acon.domain.model.onboarding

data class OnboardingPreferences(
    val dislikeFoodList: Set<String>,
    val favoriteCuisineRank: List<String>,
    val favoriteSpotType: String,
    val favoriteSpotStyle: String,
    val favoriteSpotRank: List<String>
)
