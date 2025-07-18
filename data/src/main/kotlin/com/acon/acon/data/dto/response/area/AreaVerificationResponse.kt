package com.acon.acon.data.dto.response.area

import com.acon.acon.domain.model.area.Area
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AreaVerificationResponse(
    @SerialName("verifiedAreaId") val verifiedAreaId: Long,
    @SerialName("name") val name: String
) {
    fun toArea() = Area(
        verifiedAreaId = verifiedAreaId,
        name = name
    )
}