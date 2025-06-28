package com.acon.acon.core.model.model.profile

data class ProfileInfo(
    val image: String,
    val nickname: String,
    val birthDate: String?,
    val savedSpots: List<com.acon.acon.core.model.model.profile.SavedSpot>,
) {
    companion object {
        val Empty = com.acon.acon.core.model.model.profile.ProfileInfo("", "", null, emptyList())
    }
}

data class PreSignedUrl(
    val fileName: String,
    val preSignedUrl: String
)