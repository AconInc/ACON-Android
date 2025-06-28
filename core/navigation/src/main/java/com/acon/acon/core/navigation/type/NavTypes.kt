package com.acon.acon.core.navigation.type

import com.acon.core.model.spot.SimpleSpot
import com.acon.core.model.spot.SpotNavigationParameter
import kotlin.reflect.typeOf

val simpleSpotNavType by lazy {
    typeOf<SimpleSpot>() to createNavType<SimpleSpot>()
}

val spotNavigationParameterNavType by lazy {
    typeOf<SpotNavigationParameter>() to createNavType<SpotNavigationParameter>()
}