package com.acon.acon.feature.settings.screen

import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude

fun amplitudeRetryOnboarding() {
    AconAmplitude.trackEvent(
        eventName = "onboarding",
        properties = mapOf("retry_onboarding?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "onboarding",
        properties = mapOf("retry_onboarding?" to true)
    )
}