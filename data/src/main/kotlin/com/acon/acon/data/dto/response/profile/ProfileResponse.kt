package com.acon.acon.data.dto.response.profile

import com.acon.acon.domain.model.profile.ProfileInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    @SerialName("profileImage") val image: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("birthDate") val birthDate: String? = null,
    @SerialName("savedSpotList") val savedSpotList: List<SavedSpotResponse>,
) {
    fun toProfile() = ProfileInfo(
        image = image,
        nickname = nickname,
        birthDate = birthDate,
        savedSpots = savedSpotList.map { it.toSavedSpot() }
    )
}