package com.acon.core.ui.ext

import com.acon.core.type.FoodType
import com.acon.acon.core.designsystem.R

fun FoodType.getNameResId(): Int {
    return when (this) {
        FoodType.SHRIMP -> R.string.shrimp
        FoodType.CRAB -> R.string.crab
        FoodType.CLAM -> R.string.clam
        FoodType.OYSTER -> R.string.oyster
        FoodType.SASHIMI -> R.string.raw_fish
        FoodType.FISH -> R.string.fish
        FoodType.SEAFOOD -> R.string.seafood
        FoodType.YUKHOE_YUKSASHIMI -> R.string.yukhoe
        FoodType.SEONJI -> R.string.blood_curd
        FoodType.SUNDAE -> R.string.sundae
        FoodType.GOPCHANG_DAECHANG_MAKCHANG -> R.string.beef_tripe
        FoodType.DAKBAL -> R.string.chicken_feet
        FoodType.DAKTTONGJIP -> R.string.chicken_gizzard
        FoodType.LAMB -> R.string.lamb
        FoodType.OFFAL -> R.string.pork_beef_offal
        FoodType.VEGETABLE -> R.string.vegetables
    }
}