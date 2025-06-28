package com.acon.acon.data.dto.response.area

import com.acon.acon.core.model.model.area.LegalArea
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LegalAreaResponse(
    @SerialName("area") val area: String
) {
    fun toLegalArea() = com.acon.acon.core.model.model.area.LegalArea(
        area = area
    )
}