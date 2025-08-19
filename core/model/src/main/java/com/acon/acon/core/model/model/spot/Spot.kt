package com.acon.acon.core.model.model.spot

import com.acon.acon.core.model.serializer.LocalTimeSerializer
import com.acon.acon.core.model.type.TagType
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class Spot(
    val id: Long,
    val image: String,
    val name: String,
    val acorn: Int,
    val isOpen: Boolean,
    @Serializable(with = com.acon.acon.core.model.serializer.LocalTimeSerializer::class) val closingTime: LocalTime,
    @Serializable(with = com.acon.acon.core.model.serializer.LocalTimeSerializer::class) val nextOpening: LocalTime,
    val tags: List<com.acon.acon.core.model.type.TagType>,
    val eta: Int,
    val latitude: Double,
    val longitude: Double,
)