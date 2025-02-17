package com.acon.acon.domain.model.upload

data class Suggestions(
    val suggestionList: List<Suggestion>
)

data class Suggestion(
    val spotId: Long,
    val spotName: String
)
