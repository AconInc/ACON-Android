package com.acon.acon.core.designsystem.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @param durationMillis 애니메이션 지속 시간
 * @param delayMillis 애니메이션 시작 전 딜레이
 */
fun Modifier.slidingFadeIn(
    durationMillis: Int = 500,
    delayMillis: Int = 0,
    startYOffset: Float = 50f,
    endYOffset: Float = 0f,
    startAlpha: Float = 0f,
    endAlpha: Float = 1f
): Modifier = composed {

    val alphaAnimation = remember { Animatable(startAlpha) }
    val offsetAnimation = remember { Animatable(startYOffset) }

    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        launch {
            alphaAnimation.animateTo(
                targetValue = endAlpha,
                animationSpec = tween(durationMillis = durationMillis)
            )
        }
        launch {
            offsetAnimation.animateTo(
                targetValue = endYOffset,
                animationSpec = tween(durationMillis = durationMillis)
            )
        }
    }

    this.graphicsLayer {
        this.alpha = alphaAnimation.value
        this.translationY = offsetAnimation.value
    }
}