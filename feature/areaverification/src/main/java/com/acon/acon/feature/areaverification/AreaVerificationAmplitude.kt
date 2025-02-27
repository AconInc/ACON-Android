package com.acon.acon.feature.areaverification

import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude

fun amplitudeClickNext() {
    AconAmplitude.trackEvent(
        eventName = "area_verified",
        properties = mapOf("click_area_next?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "area_verified",
        properties = mapOf("click_area_next?" to true)
    )
}

fun amplitudeCompleteArea() {
    AconAmplitude.trackEvent(
        eventName = "area_verified",
        properties = mapOf("complete_area?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "area_verified",
        properties = mapOf("complete_area?" to true)
    )
}

fun amplitudeClickGoHome() {
    AconAmplitude.trackEvent(
        eventName = "area_verified",
        properties = mapOf("click_go_home?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "area_verified",
        properties = mapOf("click_go_home?" to true)
    )
}