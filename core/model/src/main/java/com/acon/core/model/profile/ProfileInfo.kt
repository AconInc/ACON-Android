package com.acon.core.model.profile

data class ProfileInfo(
    val image: String,
    val nickname: String,
    val birthDate: String?,
    val savedSpots: List<SavedSpot>,
) {
    companion object {
        val Empty = ProfileInfo("", "", null, emptyList())
    }
}

data class PreSignedUrl(
    val fileName: String,
    val preSignedUrl: String
)