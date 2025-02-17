package com.acon.acon.data.dto.response.upload

import com.acon.acon.domain.model.upload.Suggestion
import com.acon.acon.domain.model.upload.Suggestions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadGetSuggestionsResponse(
    @SerialName("suggestionList")
    val suggestionList: List<SuggestionDto>
) {
    fun toSuggestions() = Suggestions(
        suggestionList = suggestionList.map { it.toSuggestion() }
    )
}

@Serializable
data class SuggestionDto(
    @SerialName("spotId")
    val spotId: Long,
    @SerialName("spotName")
    val spotName: String
) {
    fun toSuggestion() = Suggestion(
        spotId = spotId,
        spotName = spotName
    )
}