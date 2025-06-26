package com.acon.core.model.spot

import com.acon.core.type.TransportMode

data class SpotList(
    val transportMode: TransportMode,
    val spots: List<Spot>,
)