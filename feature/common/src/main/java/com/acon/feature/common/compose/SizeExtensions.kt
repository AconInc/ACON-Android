package com.acon.feature.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

/**
 * Px을 Dp로 변환
 */
@Composable
fun Float.toDp(): Dp {
    return with(LocalDensity.current) {
        toDp()
    }
}

/**
 * Dp을 Px로 변환
 */
@Composable
fun Dp.toPx(): Float {
    return with(LocalDensity.current) {
        toPx()
    }
}