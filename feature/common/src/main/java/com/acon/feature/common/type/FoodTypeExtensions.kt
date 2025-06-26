package com.acon.feature.common.type

import com.acon.core.type.FoodType
import com.acon.acon.core.designsystem.R

fun com.acon.core.type.FoodType.getNameResId(): Int {
    return when (this) {
        com.acon.core.type.FoodType.SHRIMP -> R.string.shrimp
        com.acon.core.type.FoodType.CRAB -> R.string.crab
        com.acon.core.type.FoodType.CLAM -> R.string.clam
        com.acon.core.type.FoodType.OYSTER -> R.string.oyster
        com.acon.core.type.FoodType.SASHIMI -> R.string.raw_fish
        com.acon.core.type.FoodType.FISH -> R.string.fish
        com.acon.core.type.FoodType.SEAFOOD -> R.string.seafood
        com.acon.core.type.FoodType.YUKHOE_YUKSASHIMI -> R.string.yukhoe
        com.acon.core.type.FoodType.SEONJI -> R.string.blood_curd
        com.acon.core.type.FoodType.SUNDAE -> R.string.sundae
        com.acon.core.type.FoodType.GOPCHANG_DAECHANG_MAKCHANG -> R.string.beef_tripe
        com.acon.core.type.FoodType.DAKBAL -> R.string.chicken_feet
        com.acon.core.type.FoodType.DAKTTONGJIP -> R.string.chicken_gizzard
        com.acon.core.type.FoodType.LAMB -> R.string.lamb
        com.acon.core.type.FoodType.OFFAL -> R.string.pork_beef_offal
        com.acon.core.type.FoodType.VEGETABLE -> R.string.vegetables
    }
}