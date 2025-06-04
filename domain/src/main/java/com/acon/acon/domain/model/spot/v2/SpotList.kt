package com.acon.acon.domain.model.spot.v2

import androidx.compose.runtime.Immutable
import com.acon.acon.domain.type.TransportMode

@Immutable
data class SpotList(
    val transportMode: TransportMode,
    val spots: List<Spot>,
)