package com.acon.android.data.dto.response.upload

import com.acon.android.domain.model.upload.SpotVerification
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadGetSpotVerifyResponse(
    @SerialName("success")
    val success: Boolean
) {
    fun toSpotVerification() = SpotVerification(
        success = success
    )
}
