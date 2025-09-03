package com.acon.core.data.dto.response.profile

import com.acon.acon.core.model.model.profile.SavedSpotLegacy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavedSpotsResponseLegacy(
    @SerialName("savedSpotList") val savedSpotResponseLegacyList: List<SavedSpotResponseLegacy>?
)

@Serializable
data class SavedSpotResponseLegacy(
    @SerialName("spotId") val spotId: Long?,
    @SerialName("name") val name: String?,
    @SerialName("image") val image: String?
) {

    fun toSavedSpot() = SavedSpotLegacy(
        spotId = spotId ?: 0L,
        name = name.orEmpty(),
        image = image.orEmpty()
    )
}