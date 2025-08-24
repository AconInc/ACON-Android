package com.acon.acon.core.ui.compose

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Pager의 스와이프 애니메이션 제거
 * @param pagerState pagerState
 * @param flingThreshold 스와이프 속도 임계. 이 값보다 빨라야 페이지가 넘어감.
 */
fun Modifier.disableSwipeAnimation(
    pagerState: PagerState,
    flingThreshold: Float = 400f
) = this.composed {
    val scope = rememberCoroutineScope()

    pointerInput(pagerState) {
        val velocityTracker = VelocityTracker()
        awaitEachGesture {
            val down = awaitFirstDown()
            velocityTracker.resetTracking()
            velocityTracker.addPosition(down.uptimeMillis, down.position)

            var isHorizontalDrag = false

            do {
                val event = awaitPointerEvent()
                val horizontalSlop = event.changes.first().positionChange().x
                val verticalSlop = event.changes.first().positionChange().y

                if (!isHorizontalDrag && abs(horizontalSlop) > abs(verticalSlop)) {
                    isHorizontalDrag = true
                }

                if (isHorizontalDrag) {
                    val firstChange = event.changes.first()
                    velocityTracker.addPosition(firstChange.uptimeMillis, firstChange.position)

                    event.changes.forEach { it.consume() }
                }
            } while (event.changes.any { it.pressed })

            val velocity = velocityTracker.calculateVelocity().x

            if (abs(velocity) > flingThreshold) {
                scope.launch {
                    if (velocity < 0) { // 왼쪽으로 스와이프
                        pagerState.scrollToPage(
                            (pagerState.currentPage + 1).coerceAtMost(pagerState.pageCount - 1)
                        )
                    } else { // 오른쪽으로 스와이프
                        pagerState.scrollToPage(
                            (pagerState.currentPage - 1).coerceAtLeast(0)
                        )
                    }
                }
            }
        }
    }
}
