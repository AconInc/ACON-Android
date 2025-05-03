package com.acon.acon.domain.model.spot.v2

import androidx.compose.runtime.Immutable

@Immutable
data class SpotV2(
    val id: Long,
    val name: String,
    val image: String,
    val dotori: String,
    val walkingTime: String,
)
