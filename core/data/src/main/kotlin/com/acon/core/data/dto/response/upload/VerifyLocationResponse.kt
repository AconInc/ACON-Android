package com.acon.acon.data.dto.response.upload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyLocationResponse(
    @SerialName("available") val isPossible: Boolean
)
