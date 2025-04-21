package com.acon.acon.feature.spot.type

import androidx.annotation.StringRes
import com.acon.acon.feature.spot.R

enum class RestaurantPriceRangeType(
    @StringRes val titleResId: Int,
    val value: Int?,
) {
    UNDER_5000(titleResId = R.string.under_5000, 5000),
    UNDER_10000(titleResId = R.string.under_10000, 10000),
    UNDER_30000(titleResId = R.string.under_30000, 30000),
    UNDER_50000(titleResId = R.string.under_50000, 50000),
    OVER_50000(titleResId = R.string.over_50000, null),
}