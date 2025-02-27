package com.acon.acon.feature.upload.amplitude

import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude

fun uploadAmplitudeSearch() {
    AconAmplitude.trackEvent(
        eventName = "place_upload",
        properties = mapOf("click_review_next?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "place_upload",
        properties = mapOf("click_review_next?" to true)
    )
}

fun uploadAmplitudeReview(aconNumber: Int, spotId: Long) {
    AconAmplitude.trackEvent(
        eventName = "place_upload",
        properties = mapOf("spot_id" to spotId)
    )
    AconTestAmplitude.trackEvent(
        eventName = "place_upload",
        properties = mapOf("spot_id" to spotId)
    )

    AconAmplitude.trackEvent(
        eventName = "place_upload",
        properties = mapOf("num_of_acon" to aconNumber)
    )
    AconTestAmplitude.trackEvent(
        eventName = "place_upload",
        properties = mapOf("num_of_acon" to aconNumber)
    )

    AconAmplitude.trackEvent(
        eventName = "place_upload",
        properties = mapOf("click_review_acon?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "place_upload",
        properties = mapOf("click_review_acon?" to true)
    )
}