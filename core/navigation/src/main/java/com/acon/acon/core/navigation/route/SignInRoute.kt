package com.acon.acon.core.navigation.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface SignInRoute {

    @Serializable
    data object Graph : SignInRoute

    @Serializable
    data object SignIn : SignInRoute
}