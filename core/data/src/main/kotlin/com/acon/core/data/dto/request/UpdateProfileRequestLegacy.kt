package com.acon.core.data.dto.request

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequestLegacy @OptIn(ExperimentalSerializationApi::class) constructor(
    @SerialName("profileImage") val profileImage: String? = null,
    @SerialName("nickname") val nickname: String,
    @SerialName("birthDate") @EncodeDefault(EncodeDefault.Mode.NEVER) val birthDate: String? = null
)