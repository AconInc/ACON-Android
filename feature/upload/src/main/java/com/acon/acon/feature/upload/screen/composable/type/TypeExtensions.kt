package com.acon.acon.feature.upload.screen.composable.type

import com.acon.acon.core.designsystem.R
import com.acon.acon.core.model.type.CafeFeatureType
import com.acon.acon.core.model.type.PriceFeatureType
import com.acon.acon.core.model.type.RestaurantFeatureType
import com.acon.acon.core.model.type.SpotType

internal fun SpotType.getNameResId(): Int {
    return when (this) {
        SpotType.RESTAURANT -> R.string.restaurant
        SpotType.CAFE -> R.string.cafe
    }
}

internal fun PriceFeatureType.PriceOptionType.getNameResId(): Int {
    return when (this) {
        PriceFeatureType.PriceOptionType.VALUE_FOR_MONEY -> R.string.upload_place_select_price_option_1
        PriceFeatureType.PriceOptionType.AVERAGE_VALUE -> R.string.upload_place_select_price_option_2
        PriceFeatureType.PriceOptionType.LOW_VALUE -> R.string.upload_place_select_price_option_3
    }
}

internal fun RestaurantFeatureType.RestaurantType.getNameResId(): Int {
    return when (this) {
        RestaurantFeatureType.RestaurantType.KOREAN -> R.string.korean
        RestaurantFeatureType.RestaurantType.CHINESE -> R.string.chinese
        RestaurantFeatureType.RestaurantType.JAPANESE -> R.string.japanese
        RestaurantFeatureType.RestaurantType.WESTERN -> R.string.western
        RestaurantFeatureType.RestaurantType.SOUTHEAST_ASIAN -> R.string.asian
        RestaurantFeatureType.RestaurantType.FUSION -> R.string.fusion
        RestaurantFeatureType.RestaurantType.BUNSIK -> R.string.korean_street
        RestaurantFeatureType.RestaurantType.BUFFET -> R.string.buffet
        RestaurantFeatureType.RestaurantType.DRINKING_PLACE -> R.string.drink_bar
        RestaurantFeatureType.RestaurantType.OTHERS -> R.string.reason_other
    }
}

internal fun CafeFeatureType.CafeType.getNameResId(): Int {
    return when(this) {
        CafeFeatureType.CafeType.GOOD_FOR_WORK -> R.string.upload_place_select_cafe_option1
        CafeFeatureType.CafeType.NOT_GOOD_FOR_WORK -> R.string.upload_place_select_cafe_option2
    }
}