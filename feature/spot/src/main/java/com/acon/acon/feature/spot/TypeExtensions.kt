package com.acon.acon.feature.spot

import com.acon.acon.core.designsystem.R
import com.acon.acon.core.model.type.CafeFilterType
import com.acon.acon.core.model.type.FilterType
import com.acon.acon.core.model.type.RestaurantFilterType
import com.acon.acon.core.model.type.SpotType

internal fun com.acon.acon.core.model.type.SpotType.getNameResId(): Int {
    return when (this) {
        com.acon.acon.core.model.type.SpotType.RESTAURANT -> R.string.restaurant
        com.acon.acon.core.model.type.SpotType.CAFE -> R.string.cafe
    }
}

internal fun com.acon.acon.core.model.type.FilterType.getNameResId(): Int {
    return when (this) {
        is com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType -> {
            when(this) {
                com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType.KOREAN -> com.acon.acon.core.designsystem.R.string.korean
                com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType.CHINESE -> com.acon.acon.core.designsystem.R.string.chinese
                com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType.JAPANESE -> com.acon.acon.core.designsystem.R.string.japanese
                com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType.WESTERN -> com.acon.acon.core.designsystem.R.string.western
                com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType.ASIAN -> com.acon.acon.core.designsystem.R.string.asian
                com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType.FUSION -> com.acon.acon.core.designsystem.R.string.fusion
                com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType.BUNSIK -> com.acon.acon.core.designsystem.R.string.korean_street
                com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType.BUFFET -> com.acon.acon.core.designsystem.R.string.buffet
                com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType.DRINKING_PLACE -> com.acon.acon.core.designsystem.R.string.drink_bar
                com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType.EXCLUDE_FRANCHISE -> com.acon.acon.core.designsystem.R.string.exclude_franchise
            }
        }
        is com.acon.acon.core.model.type.CafeFilterType.CafeType -> {
            when(this) {
                com.acon.acon.core.model.type.CafeFilterType.CafeType.WORK_FRIENDLY -> com.acon.acon.core.designsystem.R.string.for_work
                com.acon.acon.core.model.type.CafeFilterType.CafeType.EXCLUDE_FRANCHISE -> com.acon.acon.core.designsystem.R.string.exclude_franchise
            }
        }
        is com.acon.acon.core.model.type.RestaurantFilterType.RestaurantOperationType -> {
            when(this) {
                com.acon.acon.core.model.type.RestaurantFilterType.RestaurantOperationType.OPEN_AFTER_10PM -> com.acon.acon.core.designsystem.R.string.after_10pm
            }
        }
        is com.acon.acon.core.model.type.CafeFilterType.CafeOperationType -> {
            when(this) {
                com.acon.acon.core.model.type.CafeFilterType.CafeOperationType.OPEN_AFTER_10PM -> com.acon.acon.core.designsystem.R.string.after_10pm
            }
        }
        is com.acon.acon.core.model.type.RestaurantFilterType.RestaurantPriceType -> {
            when(this) {
                com.acon.acon.core.model.type.RestaurantFilterType.RestaurantPriceType.VALUE_FOR_MONEY -> com.acon.acon.core.designsystem.R.string.cost_effectiveness
            }
        }

        else -> throw IllegalArgumentException("Unknown filter type")
    }
}