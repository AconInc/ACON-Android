package com.acon.acon.data.dto.response.area

import com.acon.core.model.area.LegalArea
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LegalAreaResponse(
    @SerialName("area") val area: String
) {
    fun toLegalArea() = LegalArea(
        area = area
    )
}