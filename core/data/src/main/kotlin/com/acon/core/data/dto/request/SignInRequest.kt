package com.acon.core.data.dto.request

import com.acon.acon.core.model.type.SocialType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    @SerialName("socialType") val socialType: SocialType?,
    @SerialName("idToken") val idToken: String?,
)
