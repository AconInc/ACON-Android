package com.acon.acon.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewRequest(
    @SerialName("spotId") val spotId: Long,
    @SerialName("acornCount") val acornCount: Int
)

@Serializable
data class ReviewRequestV2(
    @SerialName("spotId") val spotId: Long,
    @SerialName("recommendedMenu") val recommendedMenu: String,
    @SerialName("acornCount") val acornCount: Int
)