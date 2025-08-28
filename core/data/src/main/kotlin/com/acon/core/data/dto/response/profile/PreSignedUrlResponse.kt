package com.acon.core.data.dto.response.profile

import com.acon.acon.core.model.model.profile.PreSignedUrl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreSignedUrlResponse(
    @SerialName("fileName") val fileName: String,
    @SerialName("preSignedUrl") val preSignedUrl: String,
) {
    fun toPreSignedUrl() = PreSignedUrl(
        fileName = fileName,
        preSignedUrl = preSignedUrl,
    )
}