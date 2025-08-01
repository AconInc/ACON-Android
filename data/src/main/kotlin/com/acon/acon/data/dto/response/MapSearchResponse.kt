package com.acon.acon.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MapSearchResponse(
    @SerialName("items") val placeList: List<MapSearchPlaceResponse>
)

@Serializable
data class MapSearchPlaceResponse(
    @SerialName("title") val title: String,
    @SerialName("address") val address: String
)