package com.acon.acon.feature.onboarding.type

import androidx.annotation.DrawableRes
import com.acon.acon.feature.onboarding.R

interface CardItem {
    val imageResId: Int
    val cardName: String
    val id: String
}

enum class FoodItems(
    @DrawableRes override val imageResId: Int,
    override val cardName: String,
    override val id: String,
) : CardItem {

    DAKBAL(R.drawable.food_img_1, cardName = "닭발", id = "DAKBAL"),
    RAW(R.drawable.food_img_2, cardName = "회/육회", id = "HOE_YUKHOE"),
    INTESTINES(R.drawable.food_img_3, cardName = "곱창/대창/막창", id = "GOPCHANG_MAKCHANG_DAECHANG"),
    SUNDAE(R.drawable.food_img_4, cardName = "순대/선지", id = "SUNDAE_SEONJI"),
    SHEEP(R.drawable.food_img_5, cardName = "양고기", id = "YANGGOGI"),
    NONE(0, cardName = "없음", id = "")
}

enum class FoodTypeItems(
    @DrawableRes override val imageResId: Int,
    override val cardName: String,
    override val id: String,
) : CardItem {
    KOREAN(R.drawable.food_korean, cardName = "한식", id = "KOREAN"),
    AMERICAN(R.drawable.food_american, cardName = "양식", id = "WESTERN"),
    CHINESE(R.drawable.food_chinese, cardName = "중식", id = "CHINESE"),
    JAPANESE(R.drawable.food_japanese, cardName = "일식", id = "JAPANESE"),
    STREET(R.drawable.food_street, cardName = "분식", id = "KOREAN_STREET"),
    ASIAN(R.drawable.food_asian, cardName = "아시안", id = "ASIAN"),
}
