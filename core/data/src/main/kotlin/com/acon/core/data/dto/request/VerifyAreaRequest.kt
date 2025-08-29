package com.acon.core.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyAreaRequest(
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double
)
