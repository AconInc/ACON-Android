package com.acon.acon.feature.spot.type

import androidx.annotation.StringRes
import com.acon.acon.feature.spot.R

enum class AvailableWalkingTimeType(
    @StringRes val titleResId: Int,
    val value: Int?
) {
    UNDER_5_MINUTES(titleResId = R.string.under_5_minutes, 5),
    UNDER_10_MINUTES(titleResId = R.string.under_10_minutes, 10),
    UNDER_15_MINUTES(titleResId = R.string.under_15_minutes, 15),
    UNDER_20_MINUTES(titleResId = R.string.under_20_minutes, 20),
    OVER_20_MINUTES(titleResId = R.string.over_20_minutes, 25),
}