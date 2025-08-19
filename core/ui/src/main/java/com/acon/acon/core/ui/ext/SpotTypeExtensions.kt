package com.acon.acon.core.ui.ext

import com.acon.acon.core.model.type.SpotType
import com.acon.acon.core.designsystem.R

fun SpotType.getNameResId(): Int {
    return when (this) {
        SpotType.RESTAURANT -> R.string.restaurant
        SpotType.CAFE -> R.string.cafe
    }
}