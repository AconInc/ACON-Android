package com.acon.acon.domain.model.profile

import androidx.compose.runtime.Immutable

@Immutable
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

@Immutable
data class PreSignedUrl(
    val fileName: String,
    val preSignedUrl: String
)