package com.acon.acon.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AreaVerificationRequest(
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double
)
