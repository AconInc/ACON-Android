package com.acon.acon.data.dto.response

import com.acon.acon.core.model.model.user.RefreshToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshResponse(
    @SerialName("accessToken") val accessToken: String?,
    @SerialName("refreshToken") val refreshToken: String?,
) {
    fun toRefreshToken() = com.acon.acon.core.model.model.user.RefreshToken(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
}