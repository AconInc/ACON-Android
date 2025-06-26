package com.acon.core.model.spot

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class SimpleSpot(
    val spotId: Long,
    val name: String
)