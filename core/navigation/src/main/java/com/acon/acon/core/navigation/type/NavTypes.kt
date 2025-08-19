package com.acon.acon.core.navigation.type

import com.acon.acon.core.model.model.spot.SimpleSpot
import com.acon.acon.core.model.model.spot.SpotNavigationParameter
import kotlin.reflect.typeOf

val simpleSpotNavType by lazy {
    typeOf<com.acon.acon.core.model.model.spot.SimpleSpot>() to createNavType<com.acon.acon.core.model.model.spot.SimpleSpot>()
}

val spotNavigationParameterNavType by lazy {
    typeOf<com.acon.acon.core.model.model.spot.SpotNavigationParameter>() to createNavType<com.acon.acon.core.model.model.spot.SpotNavigationParameter>()
}