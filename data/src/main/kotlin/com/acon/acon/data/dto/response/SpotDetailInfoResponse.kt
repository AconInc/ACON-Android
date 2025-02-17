package com.acon.acon.data.dto.response

import com.acon.acon.domain.model.spot.SpotDetailInfo
import com.acon.acon.domain.type.SpotType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpotDetailInfoResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("spotType") val spotType: SpotType,
    @SerialName("imageList") val imageList: List<String>,
    @SerialName("openStatus") val openStatus: Boolean,
    @SerialName("address") val address: String,
    @SerialName("localAcornCount") val localAcornCount: Int,
    @SerialName("basicAcornCount") val basicAcornCount: Int,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
) {
    fun toSpotDetailInfo() = SpotDetailInfo(
        id = id,
        name = name,
        spotType = spotType,
        imageList = imageList,
        openStatus = openStatus,
        address = address,
        localAcornCount = localAcornCount,
        basicAcornCount = basicAcornCount,
        latitude = latitude,
        longitude = longitude
    )
}
