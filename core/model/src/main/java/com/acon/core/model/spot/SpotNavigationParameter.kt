package com.acon.core.model.spot

import androidx.compose.runtime.Immutable
import com.acon.core.type.TagType
import com.acon.core.type.TransportMode
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class SpotNavigationParameter(
    val spotId: Long,
    val tags: List<TagType>,
    val transportMode: TransportMode?,
    val eta: Int?,
    val isFromDeepLink: Boolean?,
    val navFromProfile: Boolean?
)