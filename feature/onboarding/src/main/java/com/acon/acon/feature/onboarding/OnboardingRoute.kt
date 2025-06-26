package com.acon.acon.feature.onboarding

import kotlinx.serialization.Serializable

sealed interface OnboardingRoute {

    @Serializable
    data object Graph : OnboardingRoute

    @Serializable
    data object ChooseDislikes : OnboardingRoute
}