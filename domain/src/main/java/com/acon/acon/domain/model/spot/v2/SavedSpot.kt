package com.acon.acon.domain.model.spot.v2

import androidx.compose.runtime.Immutable

@Immutable
data class SavedSpot(
    val id: Long,
    val image: String?,
    val name: String
)
