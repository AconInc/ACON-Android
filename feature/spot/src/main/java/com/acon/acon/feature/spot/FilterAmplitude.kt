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

fun amplitudeFilterPassengerRestaurant(selectedCompanions: Set<String>) {
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

fun amplitudeFilterWalkSlideRestaurant(walkingTime: String, isDefault: Boolean) {
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

fun amplitudeFilterPriceSlideRestaurant(priceRange: String, isDefault: Boolean) {
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

fun amplitudeFilterCompleteRestaurant(isCompleteFilter: Boolean) {
    AconAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("complete_filter_restaurant?" to isCompleteFilter)
    )
    AconTestAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("complete_filter_restaurant?" to isCompleteFilter)
    )
}

// 여기부터 카페
fun amplitudeFilterCafe() {
    AconAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("choose_filter_cafe?" to true)
    )
    AconTestAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("choose_filter_cafe?" to true)
    )
}

fun amplitudeFilterVisitCafe(selectedCategories: Set<String>) {
    val categories = selectedCategories.joinToString(", ")

    AconAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("filter_visit_click_cafe?" to categories)
    )
    AconTestAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("filter_visit_click_cafe?" to categories)
    )
}

fun amplitudeFilterPurposeCafe(selectedCompanions: Set<String>) {
    val companions = selectedCompanions.joinToString(", ")

    AconAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("filter_purpose_click_cafe?" to companions)
    )
    AconTestAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("filter_purpose_click_cafe?" to companions)
    )
}

fun amplitudeFilterWalkSlideCafe(walkingTime: String, isDefault: Boolean) {
    AconAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf(
            "filter_walk_slide_cafe?" to walkingTime,
            "slide_walk_cafe?" to !isDefault
        )
    )
    AconTestAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf(
            "filter_walk_slide_cafe?" to walkingTime,
            "slide_walk_cafe?" to !isDefault
        )
    )
}

fun amplitudeFilterPriceSlideCafe(priceRange: String, isDefault: Boolean) {
    AconAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf(
            "filter_price_slide_cafe?" to priceRange,
            "slide_price_cafe?" to !isDefault
        )
    )
    AconTestAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf(
            "filter_price_slide_cafe?" to priceRange,
            "slide_price_cafe?" to !isDefault
        )
    )
}

fun amplitudeFilterCompleteCafe(isCompleteFilter: Boolean) {
    AconAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("complete_filter_cafe?" to isCompleteFilter)
    )
    AconTestAmplitude.trackEvent(
        eventName = "filter",
        properties = mapOf("complete_filter_cafe?" to isCompleteFilter)
    )
}