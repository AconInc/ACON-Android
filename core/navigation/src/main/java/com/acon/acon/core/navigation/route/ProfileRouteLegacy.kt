package com.acon.acon.core.navigation.route

import kotlinx.serialization.Serializable

interface ProfileRouteLegacy {

    @Serializable
    data object Graph : ProfileRouteLegacy

    @Serializable
    data object ProfileLegacy : ProfileRouteLegacy

    @Serializable
    data class ProfileModLegacy(val photoId: String?) : ProfileRouteLegacy

    @Serializable
    data object Bookmark : ProfileRouteLegacy
}