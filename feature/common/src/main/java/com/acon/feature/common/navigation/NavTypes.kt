package com.acon.feature.common.navigation

import com.acon.acon.domain.model.spot.NavFromBookmark
import com.acon.acon.domain.model.spot.SimpleSpot
import com.acon.acon.domain.model.spot.SpotNavigationParameter
import kotlin.reflect.typeOf

val simpleSpotNavType by lazy {
    typeOf<SimpleSpot>() to createNavType<SimpleSpot>()
}

val spotNavigationParameterNavType by lazy {
    typeOf<SpotNavigationParameter>() to createNavType<SpotNavigationParameter>()
}

val fromBookmarkNavType by lazy {
    typeOf<NavFromBookmark>() to createNavType<NavFromBookmark>()
}