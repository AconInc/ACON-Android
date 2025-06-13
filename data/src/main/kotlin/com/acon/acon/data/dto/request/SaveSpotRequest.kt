package com.acon.acon.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveSpotRequest(
    @SerialName("spotId") val spotId: Long
)