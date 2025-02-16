package com.acon.acon.data.dto.response

import com.acon.acon.domain.model.user.VerificationStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("accessToken") val accessToken: String?,
    @SerialName("refreshToken") val refreshToken: String?,
    @SerialName("hasVerifiedArea") val hasVerifiedArea: Boolean
) {
    fun toVerificationStatus() = VerificationStatus(hasVerifiedArea)
}
