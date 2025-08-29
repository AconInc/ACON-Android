package com.acon.acon.core.navigation.route

import kotlinx.serialization.Serializable

interface SettingsRoute {
    @Serializable
    data object Graph : SettingsRoute

    @Serializable
    data object Settings : SettingsRoute

    @Serializable
    data object UserVerifiedAreas : SettingsRoute

    @Serializable
    data object DeleteAccount : SettingsRoute
}