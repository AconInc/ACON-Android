package com.acon.acon.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpotListRequest(
    @SerialName("latitude") val latitude: Double?,
    @SerialName("longitude") val longitude: Double?,
    @SerialName("condition") val condition: ConditionRequest?,
)

@Serializable
data class ConditionRequest(
    @SerialName("spotType") val spotType: String?,
    @SerialName("filterList") val filterList: List<FilterListRequest>?,
    @SerialName("walkingTime") val walkingTime: Int?,
    @SerialName("priceRange") val priceRange: Int?,
)

@Serializable
data class FilterListRequest(
    @SerialName("category") val category: String?,
    @SerialName("optionList") val optionList: List<String>?,
)