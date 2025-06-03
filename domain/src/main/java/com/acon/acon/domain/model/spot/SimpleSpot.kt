package com.acon.acon.domain.model.spot

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class SimpleSpot(
    val spotId: Int,
    val name: String
)