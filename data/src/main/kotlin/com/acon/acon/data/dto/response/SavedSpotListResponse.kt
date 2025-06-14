package com.acon.acon.data.dto.response

import com.acon.acon.domain.model.spot.v2.SavedSpot
import com.acon.acon.domain.model.spot.v2.SavedSpotList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavedSpotListResponse(
    @SerialName("savedSpotList") val savedSpotList: List<SavedSpotResponse>
) {
    fun toSavedSpotList() = SavedSpotList(
        saveSpotList = savedSpotList.map { it.toSavedSpot() }
    )
}

@Serializable
data class SavedSpotResponse(
    @SerialName("spotId") val id: Long,
    @SerialName("image") val image: String? = null,
    @SerialName("name") val name: String
) {
    fun toSavedSpot() = SavedSpot(
        id = id,
        image = image,
        name = name
    )
}
