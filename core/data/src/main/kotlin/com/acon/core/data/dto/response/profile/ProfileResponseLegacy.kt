package com.acon.core.data.dto.response.profile

import com.acon.acon.core.model.model.profile.ProfileInfoLegacy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponseLegacy(
    @SerialName("profileImage") val image: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("birthDate") val birthDate: String? = null,
    @SerialName("savedSpotList") val savedSpotList: List<SavedSpotResponseLegacy>,
) {
    fun toProfile() = ProfileInfoLegacy(
        image = image,
        nickname = nickname,
        birthDate = birthDate,
        savedSpotLegacies = savedSpotList.map { it.toSavedSpot() }
    )
}