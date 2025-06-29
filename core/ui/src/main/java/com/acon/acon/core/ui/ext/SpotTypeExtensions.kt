package com.acon.acon.core.ui.ext

import com.acon.acon.core.model.type.SpotType
import com.acon.acon.core.designsystem.R

fun com.acon.acon.core.model.type.SpotType.getNameResId(): Int {
    return when (this) {
        com.acon.acon.core.model.type.SpotType.RESTAURANT -> R.string.restaurant
        com.acon.acon.core.model.type.SpotType.CAFE -> R.string.cafe
    }
}