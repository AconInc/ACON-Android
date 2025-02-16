package com.acon.acon.feature.onboarding.type

import androidx.annotation.DrawableRes
import com.acon.acon.feature.onboarding.R

enum class PlaceItems(
    @DrawableRes override val imageResId: Int,
    override val cardName: String,
    override val id: String
) : CardItem {

    RESTAURANT(R.drawable.place_img_3, cardName = "음식점", id = "RESTAURANT"),
    CAFE(R.drawable.place_img_4, cardName = "카페", id = "CAFE"),
}

enum class MoodItems(
    @DrawableRes override val imageResId: Int,
    override val cardName: String,
    override val id: String
) : CardItem {
    OLD(R.drawable.place_img_1, cardName = "빈티지", id = "VINTAGE"),
    MODERN(R.drawable.place_img_2, cardName = "모던", id = "MODERN"),
}