package com.acon.acon.feature.areaverification

import kotlinx.serialization.Serializable

@Serializable
sealed interface AreaVerificationRoute {

    @Serializable
    data object Graph : AreaVerificationRoute

    @Serializable
    data class AreaVerification(
        val verifiedAreaId: Long? = null,
        val route: String? = null
    ) : AreaVerificationRoute

    @Serializable
    data class CheckInMap(
        val latitude: Double,
        val longitude: Double,
        val verifiedAreaId: Long,
        val route: String? = null,
    ) : AreaVerificationRoute
}