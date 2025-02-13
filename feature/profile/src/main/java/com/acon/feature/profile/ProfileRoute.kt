package com.acon.feature.profile

import kotlinx.serialization.Serializable

interface ProfileRoute {

    @Serializable
    data object Graph : ProfileRoute

    @Serializable
    data object Profile : ProfileRoute

    @Serializable
    data object ProfileMod : ProfileRoute

    @Serializable
    data object GalleryList : ProfileRoute

    @Serializable
    data class GalleryGrid(val albumId: String, val albumName: String) : ProfileRoute
}