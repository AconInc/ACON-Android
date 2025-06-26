package com.acon.feature.common.navigation

import com.acon.core.model.SimpleSpot
import com.acon.core.model.SpotNavigationParameter
import kotlin.reflect.typeOf

val simpleSpotNavType by lazy {
    typeOf<com.acon.core.model.SimpleSpot>() to createNavType<com.acon.core.model.SimpleSpot>()
}

val spotNavigationParameterNavType by lazy {
    typeOf<com.acon.core.model.SpotNavigationParameter>() to createNavType<com.acon.core.model.SpotNavigationParameter>()
}