package com.acon.acon.feature.profile.composable

import kotlinx.serialization.Serializable

interface ProfileRoute {

    @Serializable
    data object Graph : ProfileRoute

    @Serializable
    data object Profile : ProfileRoute

    @Serializable
    data class ProfileMod(val photoId: String?) : ProfileRoute

    @Serializable
    data object GalleryList : ProfileRoute

    @Serializable
    data class GalleryGrid(val albumId: String, val albumName: String) : ProfileRoute

    @Serializable
    data class PhotoCrop(val photoId: String) : ProfileRoute
}