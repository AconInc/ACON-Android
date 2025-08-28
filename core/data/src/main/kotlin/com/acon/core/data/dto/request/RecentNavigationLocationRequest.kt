package com.acon.core.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecentNavigationLocationRequest(
    @SerialName("spotId") val spotId: Long,
)
