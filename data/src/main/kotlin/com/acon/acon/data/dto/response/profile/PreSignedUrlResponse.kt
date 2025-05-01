package com.acon.acon.data.dto.response.profile

import com.acon.acon.domain.model.profile.PreSignedUrl
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