package com.acon.acon.core.navigation.route

import kotlinx.serialization.Serializable

interface ProfileRoute {

    @Serializable
    data object Graph : ProfileRoute

    @Serializable
    data object Profile : ProfileRoute

    @Serializable
    data class ProfileMod(val photoId: String?) : ProfileRoute

    @Serializable
    data object Bookmark : ProfileRoute
}