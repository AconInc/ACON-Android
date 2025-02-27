package com.acon.acon.feature.spot

import com.acon.acon.core.utils.feature.amplitude.AconAmplitude
import com.acon.acon.core.utils.feature.amplitude.AconTestAmplitude

fun amplitudeClickFilter() {
    AconAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("click_filter?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("click_filter?" to true)
    )
}

fun amplitudeFilterRestaurant() {
    AconAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("choose_filter_restaurant?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("choose_filter_restaurant?" to true)
    )
}

fun amplitudeFilterVisitRestaurant(selectedCategories: Set<String>) {
    val categories = selectedCategories.joinToString(", ")

    AconAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("filter_visit_click_restaurant?" to categories)
    )
    AconTestAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("filter_visit_click_restaurant?" to categories)
    )
}

fun amplitudeFilterPassenger(selectedCompanions: Set<String>) {
    val companions = selectedCompanions.joinToString(", ")

    AconAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("filter_passenger_click_restaurant?" to companions)
    )
    AconTestAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("filter_passenger_click_restaurant?" to companions)
    )
}

fun amplitudeFilterWalkSlide(walkingTime: String, isDefault: Boolean) {
    AconAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf(
            "filter_walk_slide_restaurant?" to walkingTime,
            "slide_walk_restaurant?" to !isDefault
        )
    )
    AconTestAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf(
            "filter_walk_slide_restaurant?" to walkingTime,
            "slide_walk_restaurant?" to !isDefault
        )
    )
}

fun amplitudeFilterPriceSlide(priceRange: String, isDefault: Boolean) {
    AconAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf(
            "filter_price_slide_restaurant?" to priceRange,
            "slide_price_restaurant?" to !isDefault
        )
    )
    AconTestAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf(
            "filter_price_slide_restaurant?" to priceRange,
            "slide_price_restaurant?" to !isDefault
        )
    )
}

fun amplitudeFilterRestaurantComplete(isCompleteFilter: Boolean) {
    AconAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("complete_filter_restaurant?" to isCompleteFilter)
    )
    AconTestAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("complete_filter_restaurant?" to isCompleteFilter)
    )
}