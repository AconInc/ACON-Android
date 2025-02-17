package com.acon.acon.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteAccountRequest(
    @SerialName("reason") val reason: String,
    @SerialName("refreshToken") val refreshToken: String,
)