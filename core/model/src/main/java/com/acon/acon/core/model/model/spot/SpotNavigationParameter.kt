package com.acon.acon.core.model.model.spot

import com.acon.acon.core.model.type.TagType
import com.acon.acon.core.model.type.TransportMode
import kotlinx.serialization.Serializable

@Serializable
data class SpotNavigationParameter(
    val spotId: Long,
    val tags: List<com.acon.acon.core.model.type.TagType>,
    val transportMode: com.acon.acon.core.model.type.TransportMode?,
    val eta: Int?,
    val isFromDeepLink: Boolean?,
    val navFromProfile: Boolean?
)