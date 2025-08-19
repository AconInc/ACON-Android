package com.acon.acon.core.model.model.spot

import kotlinx.serialization.Serializable

@Serializable
data class SimpleSpot(
    val spotId: Long,
    val name: String,
    val recommendedMenu: String ?= null
)