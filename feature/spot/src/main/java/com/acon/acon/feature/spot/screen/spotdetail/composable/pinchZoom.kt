package com.acon.acon.feature.spot.screen.spotdetail.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private const val minZoomScope = 0.5f
private const val maxZoomScope = 2.5f

internal fun Modifier.pinchZoomOverlay(
    zoomState: PinchZoomState
): Modifier {
    return this
        .pointerInput(Unit) {
            coroutineScope {
                awaitEachGesture {
                    awaitFirstDown()
                    do {
                        val event = awaitPointerEvent()
                        zoomState.accumulateZoom.value *= event.calculateZoom()
                        zoomState.accumulateZoom.value =
                            zoomState.accumulateZoom.value.coerceIn(
                                minZoomScope,
                                maxZoomScope
                            )

                        if (zoomState.isZooming) {
                            zoomState.offset.value = zoomState.offset.value.plus(event.calculatePan())
                        }
                    } while (event.changes.any { it.pressed })

                    launch {
                        animateZoomReset(zoomState)
                    }
                }
            }
        }
        .onGloballyPositioned { coordinates ->
            val bounds = coordinates.boundsInWindow()
            zoomState.topLeftInWindow.value = bounds.topLeft
        }
}

internal suspend fun animateZoomReset(zoomState: PinchZoomState) {
    val animZoom = Animatable(zoomState.accumulateZoom.value)
    val animOffsetX = Animatable(zoomState.offset.value.x)
    val animOffsetY = Animatable(zoomState.offset.value.y)

    coroutineScope {
        launch {
            animZoom.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            ) {
                zoomState.accumulateZoom.value = value
            }
        }
        launch {
            animOffsetX.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            ) {
                zoomState.offset.value = zoomState.offset.value.copy(x = value)
            }
        }
        launch {
            animOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            ) {
                zoomState.offset.value = zoomState.offset.value.copy(y = value)
            }
        }
    }
}

@Composable
internal fun Modifier.transFormByZoomState(zoomState: PinchZoomState): Modifier {
    return this.graphicsLayer {
        scaleX = zoomState.accumulateZoom.value
        scaleY = zoomState.accumulateZoom.value
        translationX = zoomState.offset.value.x
        translationY = zoomState.offset.value.y
    }
}

@Composable
internal fun Modifier.pinchZoomAndTransform(zoomState: PinchZoomState): Modifier {
    return this
        .pinchZoomOverlay(zoomState)
        .transFormByZoomState(zoomState)
}

/**
 * 줌 상태
 * topLeftInWindow 줌 대상 원본 이미지의 현재 화면상 위치
 * accumulateZoom 핀치줌 누적 값
 * offset 이미지의 이동 거리 (핀치 줌 또는 드래그에 의해 이동된 오프셋)
 * url 줌 하고있는 이미지의 url
 * originHeight 줌 이미지의 화면상 원래 크기
 */
data class PinchZoomState(
    val topLeftInWindow: MutableState<Offset> = mutableStateOf(Offset(0f, 0f)),
    val accumulateZoom: MutableState<Float> = mutableFloatStateOf(1f),
    val offset: MutableState<Offset> = mutableStateOf(Offset(0f, 0f)),
    val url: String = "",
    val originHeight: Float = 0f
)

internal val PinchZoomState.isZooming: Boolean get() = accumulateZoom.value != 1f