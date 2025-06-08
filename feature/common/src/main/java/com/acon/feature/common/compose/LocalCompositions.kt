package com.acon.feature.common.compose

import android.location.Location
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import com.acon.acon.domain.type.UserType

val LocalOnRetry = staticCompositionLocalOf {
    {}
}

val LocalLocation = compositionLocalOf<Location?> {
    null
}

val LocalSnackbarHostState = staticCompositionLocalOf {
    SnackbarHostState()
}

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("CompositionLocal LocalNavController not present")
}

val LocalUserType = compositionLocalOf {
    UserType.GUEST
}

val LocalRequestSignIn = staticCompositionLocalOf {
    {}
}

val LocalRequestLocationPermission = staticCompositionLocalOf {
    {}
}