package com.acon.acon.feature.spot

import com.acon.acon.domain.model.spot.NavFromBookmark
import com.acon.acon.domain.model.spot.SpotNavigationParameter
import kotlinx.serialization.Serializable

interface SpotRoute {

    @Serializable
    data object Graph : SpotRoute

    @Serializable
    data object SpotList : SpotRoute

    @Serializable
    data class SpotDetail(
        val spotNavigationParameter: SpotNavigationParameter,
        val fromBookmark: NavFromBookmark
    ) : SpotRoute
}