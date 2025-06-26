package com.acon.acon.domain.model.upload

import androidx.compose.runtime.Immutable
import com.acon.acon.domain.type.SpotType
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class SearchedSpot(
    val spotId: Long,
    val name: String,
    val address: String,
    val spotType: SpotType,
)
