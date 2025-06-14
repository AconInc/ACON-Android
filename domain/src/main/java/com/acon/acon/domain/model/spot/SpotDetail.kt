package com.acon.acon.domain.model.spot

import androidx.compose.runtime.Immutable

@Immutable
data class SpotDetail(
    val spotId: Long,
    val imageList: List<String>,
    val name: String,
    val acornCount: Int,
    val tagList: List<String>,
    val isOpen: Boolean,
    val closingTime: String,
    val nextOpening: String,
    val hasMenuboardImage: Boolean,
    val isSaved: Boolean,
    val signatureMenuList: List<SignatureMenu>,
    val latitude: Double,
    val longitude: Double
)

@Immutable
data class SignatureMenu(
   val name: String,
   val price: Int
)