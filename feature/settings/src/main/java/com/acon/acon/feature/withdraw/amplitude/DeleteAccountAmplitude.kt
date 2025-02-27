package com.acon.acon.feature.withdraw.amplitude

import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude

fun deleteAccountAmplitudeSettingsToWithDraw() {
    AconAmplitude.trackEvent(
        eventName = "service_withdraw",
        properties = mapOf("click_exit_service?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "service_withdraw",
        properties = mapOf("click_exit_service?" to true)
    )
}

fun deleteAccountAmplitudeExitReason(exitReason: String) {
    AconAmplitude.trackEvent(
        eventName = "service_withdraw",
        properties = mapOf("exit_reaseon" to exitReason)
    )
    AconTestAmplitude.trackEvent(
        eventName = "service_withdraw",
        properties = mapOf("exit_reaseon" to exitReason)
    )
}

fun deleteAccountAmplitudeSubmit() {
    AconAmplitude.trackEvent(
        eventName = "service_withdraw",
        properties = mapOf("complete_exit_service?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "service_withdraw",
        properties = mapOf("complete_exit_service?" to true)
    )
}

fun deleteAccountAmplitudeWithDraw() {
    AconAmplitude.trackEvent(
        eventName = "service_withdraw",
        properties = mapOf("delete_id" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "service_withdraw",
        properties = mapOf("delete_id" to true)
    )
}