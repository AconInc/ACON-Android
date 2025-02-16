package com.acon.acon.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostOnboardingResultRequest(
    @SerialName("dislikeFoodList") val dislikeFoodList: Set<String>,
    @SerialName("favoriteCuisineRank") val favoriteCuisineRank: List<String>,
    @SerialName("favoriteSpotType") val favoriteSpotType: String,
    @SerialName("favoriteSpotStyle") val favoriteSpotStyle: String,
    @SerialName("favoriteSpotRank") val favoriteSpotRank: List<String>
)