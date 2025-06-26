package com.acon.core.model.spot

import kotlinx.serialization.Serializable

@Serializable
data class SimpleSpot(
    val spotId: Long,
    val name: String
)