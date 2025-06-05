package com.acon.acon.data.dto.response.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShouldUpdateResponse(
    @SerialName("forceUpdateRequired") val shouldUpdate: Boolean?
)