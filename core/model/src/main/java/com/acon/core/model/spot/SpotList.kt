package com.acon.core.model.spot

import androidx.compose.runtime.Immutable
import com.acon.core.type.TransportMode

@Immutable
data class SpotList(
    val transportMode: TransportMode,
    val spots: List<Spot>,
)