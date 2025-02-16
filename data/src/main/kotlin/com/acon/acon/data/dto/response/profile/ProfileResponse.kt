package com.acon.acon.data.dto.response.profile

import com.acon.acon.domain.model.profile.Profile
import com.acon.acon.domain.model.profile.VerifiedArea
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    @SerialName("image") val image: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("leftAcornCount") val leftAcornCount: Int,
    @SerialName("birthDate") val birthDate: String?,
    @SerialName("verifiedAreaList") val verifiedAreaList: List<VerifiedAreaResponse>,
) {
    fun toProfile() = Profile(
        image = image,
        nickname = nickname,
        leftAcornCount = leftAcornCount,
        birthDate = birthDate,
        verifiedAreaList = verifiedAreaList.map { it.toVerifiedArea() }
    )
}

@Serializable
data class VerifiedAreaResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String
) {
    fun toVerifiedArea() = VerifiedArea(
        id = id,
        name = name
    )
}
