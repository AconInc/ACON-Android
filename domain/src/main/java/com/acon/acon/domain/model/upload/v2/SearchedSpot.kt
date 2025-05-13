package com.acon.acon.domain.model.upload.v2

import androidx.compose.runtime.Immutable
import com.acon.acon.domain.type.SpotType

@Immutable
data class SearchedSpot(
    val spotId: Long,
    val name: String,
    val address: String,
    val spotType: SpotType,
)
