package com.acon.core.model.spot

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

data class SignatureMenu(
   val name: String,
   val price: Int
)