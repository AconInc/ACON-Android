package com.acon.acon.core.designsystem.component

fun amplitudeSignInonModal(){
    AconAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("did_modal_login?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("did_modal_login?" to true)
    )
}