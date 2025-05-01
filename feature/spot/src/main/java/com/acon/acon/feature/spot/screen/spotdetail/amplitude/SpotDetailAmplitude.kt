package com.acon.acon.feature.spot.screen.spotdetail.amplitude

import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude

fun spotDetailAmplitudeDuration(startTime: Long) {
    val durationInSeconds = (System.currentTimeMillis() - startTime) / 1000.0

    val properties = mapOf("click_detail_navigation?" to durationInSeconds)

    AconAmplitude.trackEvent(
        eventName = "main_menu",
        properties = properties
    )
    AconTestAmplitude.trackEvent(
        eventName = "main_menu",
        properties = properties
    )
}

fun spotDetailAmplitudeFindWay() {
    AconAmplitude.trackEvent(
        eventName = "main_menu",
        properties = mapOf("click_detail_navigation?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "main_menu",
        properties = mapOf("click_detail_navigation?" to true)
    )
}