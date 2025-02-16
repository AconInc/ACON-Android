package com.acon.acon.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AreaVerificationResponse(
    @SerialName("area") val area: String
)