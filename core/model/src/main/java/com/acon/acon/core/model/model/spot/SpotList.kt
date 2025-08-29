package com.acon.acon.core.model.model.spot

import com.acon.acon.core.model.type.TransportMode

data class SpotList(
    val transportMode: TransportMode,
    val spots: List<Spot>,
)