package com.acon.acon.data.dto.response.area

import com.acon.acon.core.model.model.area.Area
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifiedAreaListResponse(
    @SerialName("verifiedAreaList") val verifiedAreaList: List<VerifiedAreaResponse>
)

@Serializable
data class VerifiedAreaResponse(
    @SerialName("verifiedAreaId") val id: Long,
    @SerialName("name") val name: String,
) {
    fun toVerifiedArea() = com.acon.acon.core.model.model.area.Area(
        verifiedAreaId = id,
        name = name
    )
}