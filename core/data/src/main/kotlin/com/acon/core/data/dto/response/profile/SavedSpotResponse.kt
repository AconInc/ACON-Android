package com.acon.core.data.dto.response.profile

import com.acon.acon.core.model.model.profile.SavedSpot
import com.acon.acon.core.model.model.profile.SpotThumbnailStatus

data class SavedSpotResponse(
    val spotId: Long,
    val spotName: String,
    val spotThumbnail: String?
) {

    fun toSavedSpot() : SavedSpot {
        val spotThumbnailStatus = when {
            spotThumbnail.isNullOrBlank() -> SpotThumbnailStatus.Empty
            else -> SpotThumbnailStatus.Exist(spotThumbnail)
        }

        return SavedSpot(spotId, spotName, spotThumbnailStatus)
    }
}
