package com.acon.acon.core.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
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
 * Px을 Dp로 변환
 */
@Composable
fun Int.toDp(): Dp {
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

@Composable
fun getScreenWidth(): Dp = LocalWindowInfo.current.containerSize.width.toDp()

@Composable
fun getScreenHeight(): Dp = LocalWindowInfo.current.containerSize.height.toDp()