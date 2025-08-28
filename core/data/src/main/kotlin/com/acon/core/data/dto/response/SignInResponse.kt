package com.acon.core.data.dto.response

import com.acon.acon.core.model.model.user.VerificationStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    @SerialName("externalUUID") val externalUUID: String,
    @SerialName("accessToken") val accessToken: String?,
    @SerialName("refreshToken") val refreshToken: String?,
    @SerialName("hasVerifiedArea") val hasVerifiedArea: Boolean,
    @SerialName("hasPreference") val hasPreference: Boolean
) {
    fun toVerificationStatus() = VerificationStatus(
        externalUUID = externalUUID,
        hasVerifiedArea = hasVerifiedArea,
        hasPreference = hasPreference
    )
}
