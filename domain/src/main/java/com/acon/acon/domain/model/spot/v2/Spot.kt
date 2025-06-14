package com.acon.acon.domain.model.spot.v2

import androidx.compose.runtime.Immutable
import com.acon.acon.domain.serializer.LocalTimeSerializer
import com.acon.acon.domain.type.TagType
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Immutable
@Serializable
data class Spot(
    val id: Long,
    val image: String,
    val name: String,
    val acorn: Int,
    val isOpen: Boolean,
    @Serializable(with = LocalTimeSerializer::class) val closingTime: LocalTime,
    @Serializable(with = LocalTimeSerializer::class) val nextOpening: LocalTime,
    val tags: List<TagType>,
    val eta: Int,
    val latitude: Double,
    val longitude: Double,
)