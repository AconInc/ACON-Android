package com.acon.acon.feature.spot

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