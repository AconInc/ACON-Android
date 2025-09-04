package com.acon.acon.core.model.model.profile

sealed interface SpotThumbnailStatus {

    data class Exist(val url: String) : SpotThumbnailStatus
    data object Empty : SpotThumbnailStatus
}