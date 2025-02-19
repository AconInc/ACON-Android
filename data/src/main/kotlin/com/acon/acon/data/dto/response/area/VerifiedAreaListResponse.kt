package com.acon.acon.data.dto.response.area

import com.acon.acon.domain.model.area.SettingsVerifiedArea
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifiedAreaListResponse(
    @SerialName("verifiedAreaList") val verifiedAreaList: List<VerifiedAreaResponse>
)

@Serializable
data class VerifiedAreaResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
) {
    fun toVerifiedArea() = SettingsVerifiedArea(
        verifiedAreaId = id,
        name = name
    )
}