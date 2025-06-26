package com.acon.core.model.upload

import com.acon.core.type.SpotType
import kotlinx.serialization.Serializable

@Serializable
data class SearchedSpot(
    val spotId: Long,
    val name: String,
    val address: String,
    val spotType: SpotType,
)
