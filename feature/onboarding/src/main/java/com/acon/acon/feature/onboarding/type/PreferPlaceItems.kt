package com.acon.acon.feature.onboarding.type

import androidx.annotation.DrawableRes
import com.acon.acon.feature.onboarding.R

enum class PreferPlaceItems(
    @DrawableRes override val imageResId: Int,
    override val cardName: String,
    override val id: String
) : CardItem {
    MOOD(R.drawable.placetype_img_1, cardName = "분위기가 감각적인", id = "SENSE"),
    NEWFOOD(R.drawable.placetype_img_2, cardName = "새로운 음식의 경험", id = "NEW_FOOD"),
    CHEAP(R.drawable.placetype_img_3, cardName = "합리적인 가격과 양", id = "REASONABLE"),
    SPECIAL(R.drawable.placetype_img_4, cardName = "특별한 날, 고급스러운", id = "LUXURY"),
}