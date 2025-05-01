package com.acon.acon.feature.spot.type

import androidx.annotation.StringRes
import com.acon.acon.feature.spot.R

enum class CafePriceRangeType(
    @StringRes val titleResId: Int,
    val value: Int,
) {
    UNDER_3000(titleResId = R.string.under_3000, 3000),
    UNDER_5000(titleResId = R.string.under_5000, 5000),
    OVER_10000(titleResId = R.string.over_10000, 10000),
}