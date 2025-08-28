package com.acon.core.data.dto.request

import com.acon.acon.core.model.model.user.SocialPlatform
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    @SerialName("socialType") val platform: SocialPlatform?,
    @SerialName("idToken") val idToken: String?,
)
