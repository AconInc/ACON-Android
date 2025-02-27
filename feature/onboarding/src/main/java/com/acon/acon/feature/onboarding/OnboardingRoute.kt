package com.acon.acon.feature.onboarding

import kotlinx.serialization.Serializable

sealed interface OnboardingRoute {

    @Serializable
    data object Graph : OnboardingRoute

    @Serializable
    data class OnboardingScreen(val fromSettings: Boolean = false) : OnboardingRoute {
        companion object {
            fun fromSettings() = OnboardingScreen(fromSettings = true)
            fun notfromSettings() = OnboardingScreen(fromSettings = false)
        }
    }

    @Serializable
    data object LastLoading : OnboardingRoute
}