package com.acon.acon.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignOutRequest(
    @SerialName("refreshToken") val refreshToken: String
)