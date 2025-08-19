package com.acon.acon.data.dto.response.upload

import com.acon.acon.core.model.model.upload.SearchedSpot
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchedSpotsResponse(
    @SerialName("spotList") val searchedSpots: List<SearchedSpotResponse>
)

@Serializable
data class SearchedSpotResponse(
    @SerialName("spotId") val spotId: Long,
    @SerialName("name") val name: String,
    @SerialName("address") val address: String,
    @SerialName("spotType") val spotType: String
) {
    fun toSearchedSpot() = com.acon.acon.core.model.model.upload.SearchedSpot(
        spotId = spotId,
        name = name,
        address = address,
        spotType = com.acon.acon.core.model.type.SpotType.valueOf(spotType)
    )
}
