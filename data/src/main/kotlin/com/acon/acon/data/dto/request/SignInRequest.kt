package com.acon.acon.data.dto.request

import com.acon.core.type.SocialType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    @SerialName("socialType") val socialType: com.acon.core.type.SocialType?,
    @SerialName("idToken") val idToken: String?,
)
