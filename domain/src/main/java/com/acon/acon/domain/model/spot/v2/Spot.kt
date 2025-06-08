package com.acon.acon.domain.model.spot.v2

import androidx.compose.runtime.Immutable
import com.acon.acon.domain.type.TagType

@Immutable
data class Spot(
    val id: Long,
    val image: String,
    val name: String,
    val acorn: Int,
    val tags: List<TagType>,
    val eta: Int,
    val latitude: Double,
    val longitude: Double,
)