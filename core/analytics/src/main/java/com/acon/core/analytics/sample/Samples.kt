package com.acon.core.analytics.sample

import com.acon.core.analytics.EventLogger
import com.acon.core.analytics.amplitude.AconAmplitude

internal fun sampleTrack() {
    EventLogger.trackEvent(eventName = "onboarding", properties = mapOf("complete_favorite_food_rank?" to "true"))
}

internal fun sampleAmplitudeTrack() {
    AconAmplitude().trackEvent(eventName = "onboarding", properties = mapOf("complete_favorite_food_rank?" to "true"))
}

internal fun sampleAmplitudeProperties() {
    AconAmplitude().setUserProperties(properties = mapOf("favorite_food" to "pizza, chicken"))
}