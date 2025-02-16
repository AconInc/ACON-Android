package com.acon.acon.domain.model.profile

import androidx.compose.runtime.Immutable

@Immutable
data class Profile (
    val image: String,
    val nickname: String,
    val leftAcornCount: Int,
    val birthDate: String?,
    val verifiedAreaList: List<VerifiedArea>,
)

@Immutable
data class VerifiedArea(
    val id: Long,
    val name: String
)