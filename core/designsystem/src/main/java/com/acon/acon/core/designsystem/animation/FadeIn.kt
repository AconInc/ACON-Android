package com.acon.acon.core.designsystem.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import com.acon.acon.core.common.utils.delay

/**
 * @param durationMillis 애니메이션 지속 시간
 * @param delayMillis 애니메이션 시작 전 딜레이
 */
fun Modifier.fadeIn(
    durationMillis: Int = 500,
    delayMillis: Int = 0,
    startAlpha: Float = 0f,
    endAlpha: Float = 1f
): Modifier = composed {

    val alphaAnimation = remember { Animatable(startAlpha) }

    LaunchedEffect(Unit) {
        delay(delayMillis)
        alphaAnimation.animateTo(
            targetValue = endAlpha,
            animationSpec = tween(durationMillis = durationMillis)
        )
    }

    this.graphicsLayer {
        this.alpha = alphaAnimation.value
    }
}