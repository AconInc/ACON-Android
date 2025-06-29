package com.acon.acon.core.navigation.route

import kotlinx.serialization.Serializable

sealed interface OnboardingRoute {

    @Serializable
    data object Graph : OnboardingRoute

    @Serializable
    data object ChooseDislikes : OnboardingRoute
}