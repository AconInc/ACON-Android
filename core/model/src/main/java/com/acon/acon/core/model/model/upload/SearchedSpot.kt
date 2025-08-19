package com.acon.acon.core.model.model.upload

import com.acon.acon.core.model.type.SpotType
import kotlinx.serialization.Serializable

@Serializable
data class SearchedSpot(
    val spotId: Long,
    val name: String,
    val address: String,
    val spotType: com.acon.acon.core.model.type.SpotType,
)
