package com.acon.acon.domain.model.area

import androidx.compose.runtime.Immutable

@Immutable
data class SettingsVerifiedArea(
    val verifiedAreaId: Long,
    val name: String
)
