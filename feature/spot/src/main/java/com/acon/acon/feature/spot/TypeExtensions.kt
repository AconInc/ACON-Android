package com.acon.acon.feature.spot

import com.acon.acon.core.designsystem.R
import com.acon.acon.domain.type.CafeFilterType
import com.acon.acon.domain.type.FilterType
import com.acon.acon.domain.type.RestaurantFilterType
import com.acon.acon.domain.type.SpotType

internal fun SpotType.getNameResId(): Int {
    return when (this) {
        SpotType.RESTAURANT -> R.string.restaurant
        SpotType.CAFE -> R.string.cafe
    }
}

internal fun FilterType.getNameResId(): Int {
    return when (this) {
        is RestaurantFilterType.RestaurantType -> {
            when(this) {
                RestaurantFilterType.RestaurantType.KOREAN -> R.string.korean
                RestaurantFilterType.RestaurantType.CHINESE -> R.string.chinese
                RestaurantFilterType.RestaurantType.JAPANESE -> R.string.japanese
                RestaurantFilterType.RestaurantType.WESTERN -> R.string.western
                RestaurantFilterType.RestaurantType.ASIAN -> R.string.asian
                RestaurantFilterType.RestaurantType.FUSION -> R.string.fusion
                RestaurantFilterType.RestaurantType.KOREAN_STREET -> R.string.korean_street
                RestaurantFilterType.RestaurantType.BUFFET -> R.string.buffet
                RestaurantFilterType.RestaurantType.DRINK_BAR -> R.string.drink_bar
                RestaurantFilterType.RestaurantType.EXCLUDE_FRANCHISE -> R.string.exclude_franchise
            }
        }
        is CafeFilterType.CafeType -> {
            when(this) {
                CafeFilterType.CafeType.FOR_WORK -> R.string.for_work
                CafeFilterType.CafeType.EXCLUDE_FRANCHISE -> R.string.exclude_franchise
            }
        }
        is RestaurantFilterType.RestaurantOperationType -> {
            when(this) {
                RestaurantFilterType.RestaurantOperationType.AFTER_12AM -> R.string.after_12am
            }
        }
        is CafeFilterType.CafeOperationType -> {
            when(this) {
                CafeFilterType.CafeOperationType.AFTER_10PM -> R.string.after_10pm
            }
        }
        is RestaurantFilterType.RestaurantPriceType -> {
            when(this) {
                RestaurantFilterType.RestaurantPriceType.COST_EFFECTIVENESS -> R.string.cost_effectiveness
            }
        }

        else -> throw IllegalArgumentException("Unknown filter type")
    }
}