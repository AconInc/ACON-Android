package com.acon.core.model.area

import androidx.compose.runtime.Immutable

@Immutable
data class Area(
    val verifiedAreaId: Long,
    val name: String
)