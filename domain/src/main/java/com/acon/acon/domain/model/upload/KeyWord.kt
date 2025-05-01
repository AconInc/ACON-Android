package com.acon.acon.domain.model.upload

data class KeyWord(
    val spotList: List<SpotListItem>
)

data class SpotListItem(
    val spotId: Long,
    val name: String,
    val address: String,
    val spotType: String
)
