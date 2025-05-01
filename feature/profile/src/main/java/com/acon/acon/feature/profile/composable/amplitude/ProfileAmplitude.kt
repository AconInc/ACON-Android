package com.acon.acon.feature.profile.composable.amplitude

import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude

fun profileAmplitude() {
    AconAmplitude.trackEvent(
        eventName = "profile",
        properties = mapOf("did_modal_login?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "profile",
        properties = mapOf("did_modal_login?" to true)
    )
}