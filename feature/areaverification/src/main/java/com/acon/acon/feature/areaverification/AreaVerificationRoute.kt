package com.acon.acon.feature.areaverification

import kotlinx.serialization.Serializable

@Serializable
sealed interface AreaVerificationRoute {

    @Serializable
    data object Graph : AreaVerificationRoute

    @Serializable
    data object RequireAreaVerification : AreaVerificationRoute

    @Serializable
    data class CheckInMap(val latitude: Double, val longitude: Double) : AreaVerificationRoute

    @Serializable
    data object Complete : AreaVerificationRoute
}
