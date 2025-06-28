package com.acon.acon.data.dto.response

import com.acon.acon.core.model.model.spot.SignatureMenu
import com.acon.acon.core.model.model.spot.SpotDetail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpotDetailResponse(
    @SerialName("spotId") val spotId: Long,
    @SerialName("imageList") val imageList: List<String>? = null,
    @SerialName("name") val name: String,
    @SerialName("acornCount") val acornCount: Int,
    @SerialName("tagList") val tagList: List<String>? = null,
    @SerialName("isOpen") val isOpen: Boolean,
    @SerialName("closingTime") val closingTime: String,
    @SerialName("nextOpening") val nextOpening: String,
    @SerialName("hasMenuboardImage") val hasMenuboardImage: Boolean,
    @SerialName("isSaved") val isSaved: Boolean,
    @SerialName("signatureMenuList") val signatureMenuList: List<SignatureMenuResponse>? = null,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double
) {
    fun toSpotDetail() = com.acon.acon.core.model.model.spot.SpotDetail(
        spotId = spotId,
        imageList = imageList ?: emptyList(),
        name = name,
        acornCount = acornCount,
        tagList = tagList ?: emptyList(),
        isOpen = isOpen,
        closingTime = closingTime,
        nextOpening = nextOpening,
        hasMenuboardImage = hasMenuboardImage,
        isSaved = isSaved,
        signatureMenuList = signatureMenuList?.map { menu ->
            menu.toSignatureMenu()
        } ?: emptyList(),
        latitude = latitude,
        longitude = longitude
    )
}

@Serializable
data class SignatureMenuResponse(
    @SerialName("name") val name: String,
    @SerialName("price") val price: Int
) {
    fun toSignatureMenu() = com.acon.acon.core.model.model.spot.SignatureMenu(
        name = name,
        price = price
    )
}