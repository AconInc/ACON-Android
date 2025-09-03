package com.acon.acon.core.model.model.profile

data class ProfileInfoLegacy(
    val image: String,
    val nickname: String,
    val birthDate: String?,
    val savedSpotLegacies: List<com.acon.acon.core.model.model.profile.SavedSpotLegacy>,
) {
    companion object {
        val Empty = com.acon.acon.core.model.model.profile.ProfileInfoLegacy("", "", null, emptyList())
    }
}

data class PreSignedUrl(
    val fileName: String,
    val preSignedUrl: String
)