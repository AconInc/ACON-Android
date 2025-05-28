package com.acon.acon.feature.areaverification

import kotlinx.serialization.Serializable

@Serializable
sealed interface AreaVerificationRoute {

    @Serializable
    data object Graph : AreaVerificationRoute

    @Serializable
    data class AreaVerificationHome(
        val route: String? = null,
        val isEdit: Boolean = false
    ) : AreaVerificationRoute

    @Serializable
    data class CheckInMap(
        val latitude: Double,
        val longitude: Double,
        val route: String? = null,
        val isEdit: Boolean = false
    ) : AreaVerificationRoute
}