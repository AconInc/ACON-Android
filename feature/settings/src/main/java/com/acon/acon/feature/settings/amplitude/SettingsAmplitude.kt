package com.acon.acon.feature.settings.amplitude

import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude

fun settingsAmplitudeSignOut() {
    AconAmplitude.trackEvent(
        eventName = "service_logout",
        properties = mapOf("click_logout?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "service_logout",
        properties = mapOf("click_logout?" to true)
    )
}