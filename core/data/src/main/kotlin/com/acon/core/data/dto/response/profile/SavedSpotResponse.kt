package com.acon.core.data.dto.response.profile

data class SavedSpotResponse(
    val spotId: Long,
    val spotName: String,
    val spotThumbnail: String?
)
