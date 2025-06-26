package com.acon.acon.data.dto.response

import com.acon.acon.core.common.utils.toLocalTime
import com.acon.core.model.spot.v2.SpotList
import com.acon.core.model.spot.v2.Spot
import com.acon.core.type.TagType
import com.acon.core.type.TransportMode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import timber.log.Timber
import java.time.LocalTime

@Serializable
data class SpotListResponse(
    @SerialName("transportMode") val transportMode: com.acon.core.type.TransportMode?,
    @SerialName("spotList") val spotList: List<SpotResponse>?
) {
    fun toSpotList() = com.acon.core.model.spot.v2.SpotList(
        transportMode = transportMode ?: com.acon.core.type.TransportMode.WALKING,
        spots = spotList?.map { it.toSpot() } ?: emptyList(),
    )
}

@Serializable
data class SpotResponse(
    @SerialName("spotId") val id: Long?,
    @SerialName("image") val image: String?,
    @SerialName("name") val name: String?,
    @SerialName("acornCount") val acornCount: Int?,
    @SerialName("isOpen") val isOpen: Boolean?,
    @SerialName("closingTime") val closingTime: String?,
    @SerialName("nextOpening") val nextOpening: String?,
    @SerialName("tagList") val tags: List<String>?,
    @SerialName("eta") val walkingTime: Int?,
    @SerialName("latitude") val latitude: Double?,
    @SerialName("longitude") val longitude: Double?,
) {

    fun toSpot() = com.acon.core.model.spot.v2.Spot(
        id = id ?: 0L,
        image = image.orEmpty(),
        name = name.orEmpty(),
        acorn = acornCount ?: 0,
        tags = tags?.mapNotNull { tagString ->
            when (tagString) {
                "NEW" -> com.acon.core.type.TagType.NEW
                "LOCAL" -> com.acon.core.type.TagType.LOCAL
                "TOP 1" -> com.acon.core.type.TagType.TOP_1
                "TOP 2" -> com.acon.core.type.TagType.TOP_2
                "TOP 3" -> com.acon.core.type.TagType.TOP_3
                "TOP 4" -> com.acon.core.type.TagType.TOP_4
                "TOP 5" -> com.acon.core.type.TagType.TOP_5
                else -> {
                    Timber.e("Unknown tag type: $tagString")
                    null
                }
            }
        } ?: emptyList(),
        eta = walkingTime ?: 0,
        latitude = latitude ?: 0.0,
        longitude = longitude ?: 0.0,
        closingTime = closingTime.toLocalTime() ?: LocalTime.MIN,
        isOpen = isOpen ?: false,
        nextOpening = nextOpening.toLocalTime() ?: LocalTime.MIN,
    )
}