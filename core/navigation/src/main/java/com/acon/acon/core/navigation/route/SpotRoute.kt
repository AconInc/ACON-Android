package com.acon.acon.core.navigation.route

import com.acon.acon.core.model.model.spot.SpotNavigationParameter
import kotlinx.serialization.Serializable

interface SpotRoute {

    @Serializable
    data object Graph : SpotRoute

    @Serializable
    data object SpotList : SpotRoute

    @Serializable
    data class SpotDetail(val spotNavigationParameter: com.acon.acon.core.model.model.spot.SpotNavigationParameter) : SpotRoute
}