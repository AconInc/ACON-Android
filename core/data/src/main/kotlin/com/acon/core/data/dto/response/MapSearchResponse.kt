package com.acon.core.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MapSearchResponse(
    @SerialName("items") val placeList: List<MapSearchPlaceResponse>
)

@Serializable
data class MapSearchPlaceResponse(
    @SerialName("title") val title: String,
    @SerialName("category") val category: String,
    @SerialName("address") val address: String,
    @SerialName("roadAddress") val roadAddress: String
)