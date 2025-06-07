package com.acon.acon.data.dto.response.upload

import com.acon.acon.domain.model.upload.SearchedSpot
import com.acon.acon.domain.type.SpotType
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
    fun toSearchedSpot() = SearchedSpot(
        spotId = spotId,
        name = name,
        address = address,
        spotType = SpotType.valueOf(spotType)
    )
}
