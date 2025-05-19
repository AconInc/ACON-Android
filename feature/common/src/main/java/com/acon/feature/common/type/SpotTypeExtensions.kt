package com.acon.feature.common.type

import com.acon.acon.domain.type.SpotType
import com.acon.acon.core.designsystem.R

fun SpotType.getNameResId(): Int {
    return when (this) {
        SpotType.RESTAURANT -> R.string.restaurant
        SpotType.CAFE -> R.string.cafe
    }
}