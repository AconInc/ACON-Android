package com.acon.acon.feature.onboarding.screen

import com.acon.acon.core.utils.feature.amplitude.AconAmplitude

fun logOnboardingEvent(completeProperty: String, selectedItems: Set<String>, userProperty: String) {
    AconAmplitude.trackEvent(
        "onboarding",
        mapOf(
            completeProperty to true
        )
    )

    AconAmplitude.setUserProperties(
        mapOf(
            userProperty to selectedItems.joinToString(", ")
        )
    )
}
