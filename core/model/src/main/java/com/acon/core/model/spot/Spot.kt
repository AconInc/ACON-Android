package com.acon.core.model.spot

import com.acon.core.serializer.LocalTimeSerializer
import com.acon.core.type.TagType
import kotlinx.serialization.Serializable
import java.time.LocalTime

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