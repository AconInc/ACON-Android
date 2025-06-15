package com.acon.acon.domain.model.spot

import androidx.compose.runtime.Immutable
import com.acon.acon.domain.type.TagType
import com.acon.acon.domain.type.TransportMode
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class SpotNavigationParameter(
    val spotId: Long,
    val tags: List<TagType>,
    val transportMode: TransportMode?,
    val eta: Int?,
    val navFromProfile: Boolean?
)