package com.acon.acon.amplitude

import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude

fun bottomAmplitudeSignIn() {
    AconAmplitude.trackEvent(
        eventName = "place_upload",
        properties = mapOf("did_modal_login?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "place_upload",
        properties = mapOf("did_modal_login?" to true)
    )
}

fun bottomAmplitudeUpload() {
    AconAmplitude.trackEvent(
        eventName = "place_upload",
        properties = mapOf("click_upload?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "place_upload",
        properties = mapOf("click_upload?" to true)
    )
}