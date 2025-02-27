package com.acon.acon.feature.signin.screen

import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude

fun amplitudeSignIn() {
    AconAmplitude.trackEvent(
        eventName = "login",
        properties = mapOf("did_login?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "login",
        properties = mapOf("did_login?" to true)
    )
}

