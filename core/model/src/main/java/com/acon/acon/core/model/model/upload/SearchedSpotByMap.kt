package com.acon.acon.core.model.model.upload

import kotlinx.serialization.Serializable

@Serializable
data class SearchedSpotByMap(
    val title: String,
    val address: String
)