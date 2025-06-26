package com.acon.feature.common.type

import com.acon.core.type.SpotType
import com.acon.acon.core.designsystem.R

fun com.acon.core.type.SpotType.getNameResId(): Int {
    return when (this) {
        com.acon.core.type.SpotType.RESTAURANT -> R.string.restaurant
        com.acon.core.type.SpotType.CAFE -> R.string.cafe
    }
}