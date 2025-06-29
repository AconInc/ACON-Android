package com.acon.acon.core.ui.ext

import com.acon.acon.core.model.type.FoodType
import com.acon.acon.core.designsystem.R

fun com.acon.acon.core.model.type.FoodType.getNameResId(): Int {
    return when (this) {
        com.acon.acon.core.model.type.FoodType.SHRIMP -> R.string.shrimp
        com.acon.acon.core.model.type.FoodType.CRAB -> R.string.crab
        com.acon.acon.core.model.type.FoodType.CLAM -> R.string.clam
        com.acon.acon.core.model.type.FoodType.OYSTER -> R.string.oyster
        com.acon.acon.core.model.type.FoodType.SASHIMI -> R.string.raw_fish
        com.acon.acon.core.model.type.FoodType.FISH -> R.string.fish
        com.acon.acon.core.model.type.FoodType.SEAFOOD -> R.string.seafood
        com.acon.acon.core.model.type.FoodType.YUKHOE_YUKSASHIMI -> R.string.yukhoe
        com.acon.acon.core.model.type.FoodType.SEONJI -> R.string.blood_curd
        com.acon.acon.core.model.type.FoodType.SUNDAE -> R.string.sundae
        com.acon.acon.core.model.type.FoodType.GOPCHANG_DAECHANG_MAKCHANG -> R.string.beef_tripe
        com.acon.acon.core.model.type.FoodType.DAKBAL -> R.string.chicken_feet
        com.acon.acon.core.model.type.FoodType.DAKTTONGJIP -> R.string.chicken_gizzard
        com.acon.acon.core.model.type.FoodType.LAMB -> R.string.lamb
        com.acon.acon.core.model.type.FoodType.OFFAL -> R.string.pork_beef_offal
        com.acon.acon.core.model.type.FoodType.VEGETABLE -> R.string.vegetables
    }
}