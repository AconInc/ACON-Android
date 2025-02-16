package com.acon.acon.domain.model.spot

import androidx.compose.runtime.Immutable

@Immutable
data class SpotDetailMenu(
    val id: Long,
    val name: String,
    val price: Int,
    val image: String?,
)
