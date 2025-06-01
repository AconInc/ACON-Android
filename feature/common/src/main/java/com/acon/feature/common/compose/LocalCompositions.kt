package com.acon.feature.common.compose

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf

val LocalOnRetry = staticCompositionLocalOf {
    {}
}

val LocalTrigger = compositionLocalOf {
    false
}