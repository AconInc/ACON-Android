package com.acon.core.model.profile

import androidx.compose.runtime.Immutable

@Immutable
data class SavedSpot(
    val spotId: Long,
    val name: String,
    val image: String
)