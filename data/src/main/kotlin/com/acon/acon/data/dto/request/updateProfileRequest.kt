package com.acon.acon.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class updateProfileRequest(
    @SerialName("profileImage") val profileImage: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("birthDate") val birthDate: String?
)