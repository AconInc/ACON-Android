package com.acon.acon.feature.upload.screen.composable.type

import com.acon.acon.core.designsystem.R
import com.acon.acon.core.model.type.CafeOptionType
import com.acon.acon.core.model.type.PriceOptionType
import com.acon.acon.core.model.type.RestaurantFilterType
import com.acon.acon.core.model.type.SpotType

internal fun SpotType.getNameResId(): Int {
    return when (this) {
        SpotType.RESTAURANT -> R.string.restaurant
        SpotType.CAFE -> R.string.cafe
    }
}

internal fun PriceOptionType.getNameResId(): Int {
    return when (this) {
        PriceOptionType.VALUE_FOR_MONEY -> R.string.upload_place_select_price_option_1
        PriceOptionType.AVERAGE_VALUE -> R.string.upload_place_select_price_option_2
        PriceOptionType.LOW_VALUE -> R.string.upload_place_select_price_option_3
    }
}

internal fun RestaurantFilterType.RestaurantType.getNameResId(): Int {
    return when (this) {
        RestaurantFilterType.RestaurantType.KOREAN -> R.string.korean
        RestaurantFilterType.RestaurantType.CHINESE -> R.string.chinese
        RestaurantFilterType.RestaurantType.JAPANESE -> R.string.japanese
        RestaurantFilterType.RestaurantType.WESTERN -> R.string.western
        RestaurantFilterType.RestaurantType.ASIAN -> R.string.asian
        RestaurantFilterType.RestaurantType.FUSION -> R.string.fusion
        RestaurantFilterType.RestaurantType.BUNSIK -> R.string.korean_street
        RestaurantFilterType.RestaurantType.BUFFET -> R.string.buffet
        RestaurantFilterType.RestaurantType.DRINKING_PLACE -> R.string.drink_bar
        RestaurantFilterType.RestaurantType.EXCLUDE_FRANCHISE -> R.string.reason_other
    }
}

internal fun CafeOptionType.getNameResId(): Int {
    return when(this) {
        CafeOptionType.GOOD_FOR_WORK -> R.string.upload_place_select_cafe_option_1
        CafeOptionType.NOT_GOOD_FOR_WORK -> R.string.upload_place_select_cafe_option_2
    }
}