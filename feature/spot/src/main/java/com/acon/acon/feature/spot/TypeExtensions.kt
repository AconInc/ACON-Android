package com.acon.acon.feature.spot

import com.acon.acon.domain.type.FilterType
import com.acon.acon.domain.type.OptionType
import com.acon.acon.domain.type.SpotType

internal fun SpotType.getNameResId(): Int {
    return when (this) {
        SpotType.RESTAURANT -> com.acon.acon.core.designsystem.R.string.restaurant
        SpotType.CAFE -> R.string.cafe
    }
}

internal fun OptionType.RestaurantFeatureOptionType.getNameResId(): Int {
    return when (this) {
        OptionType.RestaurantFeatureOptionType.KOREAN -> R.string.korean
        OptionType.RestaurantFeatureOptionType.WESTERN -> R.string.western
        OptionType.RestaurantFeatureOptionType.CHINESE -> R.string.chinese
        OptionType.RestaurantFeatureOptionType.JAPANESE -> R.string.japanese
        OptionType.RestaurantFeatureOptionType.KOREAN_STREET -> R.string.korean_street
        OptionType.RestaurantFeatureOptionType.ASIAN -> R.string.asian
        OptionType.RestaurantFeatureOptionType.BAR -> R.string.bar
        OptionType.RestaurantFeatureOptionType.EXCLUDE_FRANCHISE -> R.string.exclude_franchise
    }
}

internal fun OptionType.CompanionTypeOptionType.getNameResId(): Int {
    return when (this) {
        OptionType.CompanionTypeOptionType.FAMILY -> R.string.family
        OptionType.CompanionTypeOptionType.DATE -> R.string.date
        OptionType.CompanionTypeOptionType.FRIEND -> R.string.friend
        OptionType.CompanionTypeOptionType.ALONE -> R.string.alone
        OptionType.CompanionTypeOptionType.GROUP -> R.string.group
    }
}

internal fun OptionType.CafeFeatureOptionType.getNameResId(): Int {
    return when (this) {
        OptionType.CafeFeatureOptionType.LARGE -> R.string.large
        OptionType.CafeFeatureOptionType.GOOD_VIEW -> R.string.good_view
        OptionType.CafeFeatureOptionType.DESSERT -> R.string.dessert
        OptionType.CafeFeatureOptionType.TERRACE -> R.string.terrace
        OptionType.CafeFeatureOptionType.EXCLUDE_FRANCHISE -> R.string.exclude_franchise
    }
}

internal fun OptionType.VisitPurposeOptionType.getNameResId(): Int {
    return when (this) {
        OptionType.VisitPurposeOptionType.MEETING -> R.string.meeting
        OptionType.VisitPurposeOptionType.STUDY -> R.string.study
    }
}

internal fun FilterType.getNameResId(): Int {
    return when (this) {
        is FilterType.RestaurantType -> {
            when(this) {
                FilterType.RestaurantType.KOREAN -> com.acon.acon.core.designsystem.R.string.korean
                FilterType.RestaurantType.CHINESE -> com.acon.acon.core.designsystem.R.string.chinese
                FilterType.RestaurantType.JAPANESE -> com.acon.acon.core.designsystem.R.string.japanese
                FilterType.RestaurantType.WESTERN -> com.acon.acon.core.designsystem.R.string.western
                FilterType.RestaurantType.ASIAN -> com.acon.acon.core.designsystem.R.string.asian
                FilterType.RestaurantType.FUSION -> com.acon.acon.core.designsystem.R.string.fusion
                FilterType.RestaurantType.KOREAN_STREET -> com.acon.acon.core.designsystem.R.string.korean_street
                FilterType.RestaurantType.BUFFET -> com.acon.acon.core.designsystem.R.string.buffet
                FilterType.RestaurantType.DRINK_BAR -> com.acon.acon.core.designsystem.R.string.drink_bar
            }
        }
        is FilterType.CafeType -> {
            when(this) {
                FilterType.CafeType.FOR_WORK -> com.acon.acon.core.designsystem.R.string.for_work
                FilterType.CafeType.EXCLUDE_FRANCHISE -> com.acon.acon.core.designsystem.R.string.exclude_franchise
            }
        }
        is FilterType.RestaurantOperationType -> {
            when(this) {
                FilterType.RestaurantOperationType.AFTER_12AM -> com.acon.acon.core.designsystem.R.string.after_12am
            }
        }
        is FilterType.CafeOperationType -> {
            when(this) {
                FilterType.CafeOperationType.AFTER_10PM -> com.acon.acon.core.designsystem.R.string.after_10pm
            }
        }
        is FilterType.RestaurantPriceType -> {
            when(this) {
                FilterType.RestaurantPriceType.COST_EFFECTIVENESS -> com.acon.acon.core.designsystem.R.string.cost_effectiveness
            }
        }
    }
}