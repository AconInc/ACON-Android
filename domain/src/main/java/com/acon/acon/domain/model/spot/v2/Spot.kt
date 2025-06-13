package com.acon.acon.domain.model.spot.v2

import androidx.compose.runtime.Immutable
import com.acon.acon.domain.type.TagType
import java.time.LocalTime

@Immutable
data class Spot(
    val id: Long,
    val image: String,
    val name: String,
    val acorn: Int,
    val isOpen: Boolean,
    val closingTime: LocalTime,
    val nextOpening: LocalTime,
    val tags: List<TagType>,
    val eta: Int,
    val latitude: Double,
    val longitude: Double,
)