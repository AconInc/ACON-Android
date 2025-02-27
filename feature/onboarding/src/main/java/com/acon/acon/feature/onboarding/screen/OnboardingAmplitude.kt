package com.acon.acon.feature.onboarding.screen

import com.acon.acon.core.designsystem.component.textfield.AconTextField
import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude

fun amplitudeOnboarding(completeProperty: String, selectedItems: Set<String>, userProperty: String) {
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

    AconTestAmplitude.trackEvent(
        "onboarding",
        mapOf(
            completeProperty to true
        )
    )
    AconTestAmplitude.setUserProperties(
        mapOf(
            userProperty to selectedItems.joinToString(", ")
        )
    )
}
