package com.acon.feature.common.type

import com.acon.acon.domain.type.FoodType
import com.acon.acon.core.designsystem.R

fun FoodType.getNameResId(): Int {
    return when (this) {
        FoodType.Shrimp -> R.string.shrimp
        FoodType.Crab -> R.string.crab
        FoodType.Clam -> R.string.clam
        FoodType.Oyster -> R.string.oyster
        FoodType.RawFish -> R.string.raw_fish
        FoodType.Fish -> R.string.fish
        FoodType.Seafood -> R.string.seafood
        FoodType.Yukhoe -> R.string.yukhoe
        FoodType.BloodCurd -> R.string.blood_curd
        FoodType.Sundae -> R.string.sundae
        FoodType.BeefTripe -> R.string.beef_tripe
        FoodType.ChickenFeet -> R.string.chicken_feet
        FoodType.ChickenGizzard -> R.string.chicken_gizzard
        FoodType.Lamb -> R.string.lamb
        FoodType.PorkBeefOffal -> R.string.pork_beef_offal
        FoodType.Vegetables -> R.string.vegetables
    }
}