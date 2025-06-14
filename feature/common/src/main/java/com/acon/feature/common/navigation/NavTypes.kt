package com.acon.feature.common.navigation

import com.acon.acon.domain.model.spot.NavFromProfile
import com.acon.acon.domain.model.spot.SimpleSpot
import com.acon.acon.domain.model.spot.SpotNavigationParameter
import kotlin.reflect.typeOf

val simpleSpotNavType by lazy {
    typeOf<SimpleSpot>() to createNavType<SimpleSpot>()
}

val spotNavigationParameterNavType by lazy {
    typeOf<SpotNavigationParameter>() to createNavType<SpotNavigationParameter>()
}

val fromProfileNavType by lazy {
    typeOf<NavFromProfile>() to createNavType<NavFromProfile>()
}