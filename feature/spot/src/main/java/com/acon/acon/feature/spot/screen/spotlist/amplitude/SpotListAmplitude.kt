package com.acon.acon.feature.spot.screen.spotlist.amplitude

import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude

fun spotListSpotNumberAmplitude(spotNumber: Int) {
    val spotKey = when (spotNumber) {
        1 -> "click_main_first?"
        2 -> "click_main_second?"
        3 -> "click_main_third?"
        4 -> "click_main_fourth?"
        5 -> "click_main_fifth?"
        6 -> "click_main_sixth?"
        else -> null
    }

    spotKey?.let { key ->
        val properties = mapOf(key to true)

        AconAmplitude.trackEvent(
            eventName = "main_menu",
            properties = properties
        )
        AconTestAmplitude.trackEvent(
            eventName = "main_menu",
            properties = properties
        )
    }
}

fun amplitudeSpotListSignIn() {
    AconAmplitude.trackEvent(
        eventName = "main_menu",
        properties = mapOf("did_modal_login?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "main_menu",
        properties = mapOf("did_modal_login?" to true)
    )
}