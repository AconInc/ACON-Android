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
                RestaurantFilterType.RestaurantType.KOREAN -> com.acon.acon.core.designsystem.R.string.korean
                RestaurantFilterType.RestaurantType.CHINESE -> com.acon.acon.core.designsystem.R.string.chinese
                RestaurantFilterType.RestaurantType.JAPANESE -> com.acon.acon.core.designsystem.R.string.japanese
                RestaurantFilterType.RestaurantType.WESTERN -> com.acon.acon.core.designsystem.R.string.western
                RestaurantFilterType.RestaurantType.ASIAN -> com.acon.acon.core.designsystem.R.string.asian
                RestaurantFilterType.RestaurantType.FUSION -> com.acon.acon.core.designsystem.R.string.fusion
                RestaurantFilterType.RestaurantType.BUNSIK -> com.acon.acon.core.designsystem.R.string.korean_street
                RestaurantFilterType.RestaurantType.BUFFET -> com.acon.acon.core.designsystem.R.string.buffet
                RestaurantFilterType.RestaurantType.DRINKING_PLACE -> com.acon.acon.core.designsystem.R.string.drink_bar
                RestaurantFilterType.RestaurantType.EXCLUDE_FRANCHISE -> com.acon.acon.core.designsystem.R.string.exclude_franchise
            }
        }
        is CafeFilterType.CafeType -> {
            when(this) {
                CafeFilterType.CafeType.WORK_FRIENDLY -> com.acon.acon.core.designsystem.R.string.for_work
                CafeFilterType.CafeType.EXCLUDE_FRANCHISE -> com.acon.acon.core.designsystem.R.string.exclude_franchise
            }
        }
        is RestaurantFilterType.RestaurantOperationType -> {
            when(this) {
                RestaurantFilterType.RestaurantOperationType.OPEN_AFTER_10PM -> com.acon.acon.core.designsystem.R.string.after_10pm
            }
        }
        is CafeFilterType.CafeOperationType -> {
            when(this) {
                CafeFilterType.CafeOperationType.OPEN_AFTER_10PM -> com.acon.acon.core.designsystem.R.string.after_10pm
            }
        }
        is RestaurantFilterType.RestaurantPriceType -> {
            when(this) {
                RestaurantFilterType.RestaurantPriceType.VALUE_FOR_MONEY -> com.acon.acon.core.designsystem.R.string.cost_effectiveness
            }
        }

        else -> throw IllegalArgumentException("Unknown filter type")
    }
}