package com.acon.acon.core.model.model.upload

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class SearchedSpotByMap(
    val title: String,
    val category: String,
    val address: String,
    val roadAddress: String,
    val id: String = UUID.randomUUID().toString()
)