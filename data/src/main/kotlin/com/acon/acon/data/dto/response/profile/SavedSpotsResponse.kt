package com.acon.acon.data.dto.response.profile

import com.acon.acon.core.model.model.profile.SavedSpot
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavedSpotsResponse(
    @SerialName("savedSpotList") val savedSpotResponseList: List<SavedSpotResponse>?
)

@Serializable
data class SavedSpotResponse(
    @SerialName("spotId") val spotId: Long?,
    @SerialName("name") val name: String?,
    @SerialName("image") val image: String?
) {

    fun toSavedSpot() = com.acon.acon.core.model.model.profile.SavedSpot(
        spotId = spotId ?: 0L,
        name = name.orEmpty(),
        image = image.orEmpty()
    )
}