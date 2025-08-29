package com.acon.acon.core.navigation.route

import kotlinx.serialization.Serializable

@Serializable
sealed interface AreaVerificationRoute {

    @Serializable
    data object Graph : AreaVerificationRoute

    @Serializable
    data object AreaVerification : AreaVerificationRoute

    @Serializable
    data object VerifyInMap: AreaVerificationRoute
}