package com.acon.feature.common.compose

import android.location.Location
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf

val LocalOnRetry = staticCompositionLocalOf {
    {}
}

val LocalTrigger = compositionLocalOf {
    false
}

val LocalLocation = compositionLocalOf<Location?> {
    null
}

val LocalSnackbarHostState = staticCompositionLocalOf {
    SnackbarHostState()
}