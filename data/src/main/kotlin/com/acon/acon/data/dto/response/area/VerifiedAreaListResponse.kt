package com.acon.acon.data.dto.response.area

import com.acon.acon.domain.model.area.Area
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifiedAreaListResponse(
    @SerialName("verifiedAreaList") val verifiedAreaList: List<VerifiedAreaResponse>
)

@Serializable
data class VerifiedAreaResponse(
    @SerialName("verifiedAreaId") val verifiedAreaId: Long,
    @SerialName("name") val name: String,
) {
    fun toVerifiedArea() = Area(
        verifiedAreaId = verifiedAreaId,
        name = name
    )
}