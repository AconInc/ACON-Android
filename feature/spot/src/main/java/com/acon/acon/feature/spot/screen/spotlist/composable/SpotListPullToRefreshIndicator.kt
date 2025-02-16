package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.acon.acon.feature.spot.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.github.fengdai.compose.pulltorefresh.PullToRefreshState

@Composable
fun SpotListPullToRefreshIndicator(
    refreshTriggerDistance: Dp,
    state: PullToRefreshState
) {
    val refreshTriggerPx = with(LocalDensity.current) { refreshTriggerDistance.toPx() }
    val indicatorSize = 42.dp
    val indicatorHeightPx = with(LocalDensity.current) { indicatorSize.toPx() }
    val rotation: Float
    val scaleFraction: Float
    val alphaFraction: Float
    if (!state.isRefreshing) {
        val progress = (state.contentOffset / refreshTriggerPx.coerceAtLeast(1f))
            .coerceIn(0f, 1f)
        rotation = progress * 180
        scaleFraction = LinearOutSlowInEasing.transform(progress)
        alphaFraction = progress
    } else {
        val transition = rememberInfiniteTransition()
        rotation = transition.animateValue(
            0f,
            1f,
            Float.VectorConverter,
            infiniteRepeatable(
                animation = tween(
                    durationMillis = 1332, // 1 and 1/3 second
                    easing = LinearEasing
                )
            )
        ).value * 360
        scaleFraction = 1f
        alphaFraction = 1f
    }

    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_progress_w)
    )

    LottieAnimation(
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = 16.dp)
            .graphicsLayer {
                translationY = (state.contentOffset - indicatorHeightPx) / 2f
                scaleX = scaleFraction
                scaleY = scaleFraction
                alpha = alphaFraction
                rotationZ = rotation
            }
            .size(indicatorSize),
        composition = lottieComposition,
        iterations = Int.MAX_VALUE,
        isPlaying = state.isRefreshing,
    )
}