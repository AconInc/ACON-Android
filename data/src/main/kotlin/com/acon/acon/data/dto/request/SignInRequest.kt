package com.acon.acon.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    @SerialName("socialType") val socialType: com.acon.acon.core.model.type.SocialType?,
    @SerialName("idToken") val idToken: String?,
)
