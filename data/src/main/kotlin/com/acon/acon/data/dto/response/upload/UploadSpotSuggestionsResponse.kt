package com.acon.acon.data.dto.response.upload

import com.acon.acon.domain.model.upload.UploadSpotSuggestion
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadSpotSuggestionsResponse(
    @SerialName("suggestionList") val suggestionList: List<UploadSpotSuggestionResponse>
)

@Serializable
data class UploadSpotSuggestionResponse(
    @SerialName("spotId") val spotId: Long,
    @SerialName("name") val spotName: String
) {
    fun toSuggestion() = UploadSpotSuggestion(
        spotId = spotId,
        name = spotName
    )
}