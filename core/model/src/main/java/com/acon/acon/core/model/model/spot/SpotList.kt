package com.acon.acon.core.model.model.spot

import com.acon.acon.core.model.type.TransportMode

data class SpotList(
    val transportMode: com.acon.acon.core.model.type.TransportMode,
    val spots: List<com.acon.acon.core.model.model.spot.Spot>,
)