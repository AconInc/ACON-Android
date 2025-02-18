package com.acon.acon.data.dto.request

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class updateProfileRequest @OptIn(ExperimentalSerializationApi::class) constructor(
    @SerialName("profileImage") val profileImage: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("birthDate") @EncodeDefault(EncodeDefault.Mode.NEVER) val birthDate: String? = null
)