package com.acon.acon.data.dto.response

import com.acon.acon.domain.model.spot.Spot
import com.acon.acon.domain.type.SpotType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpotListResponse(
    @SerialName("spotList") val spotList: List<SpotResponse>
)

@Serializable
data class SpotResponse(
    @SerialName("id") val id: Long,
    @SerialName("image") val image: String,
    @SerialName("matchingRate") val matchingRate: Int?,
    @SerialName("type") val type: SpotType,
    @SerialName("name") val name: String,
    @SerialName("walkingTime") val walkingTime: Int,
) {

    fun toSpot() = Spot(
        id = id,
        image = image,
        matchingRate = matchingRate ?: -1,
        type = type,
        name = name,
        walkingTime = walkingTime
    )
}