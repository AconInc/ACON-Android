package com.acon.acon.core.designsystem.effect

import android.view.ViewTreeObserver
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.launch

/**
 * 포커스를 받은 UI 요소를 스크롤 가능한 영역의 보이는 범위로 자동으로 이동(스크롤)시키는 함수
 *
 * onGloballyPositioned 이용해서 현재 UI 요소(composable)의 화면 최상단(root)으로부터의 Y 좌표와
 * scrollState.value를 더해서 scrollToPosition에 animateScrollTo()에 전달하여 해당 위치로 스크롤 합니다.
 *
 * 동작 방식:
 * 1. UI 요소의 화면상 위치를 추적하여 스크롤 위치를 계산합니다.
 * 2. 해당 요소가 포커스를 받으면 계산된 위치로 스크롤합니다.
 **/
fun Modifier.bringIntoView(
    scrollState: ScrollState
): Modifier = composed {
    var scrollToPosition by remember { mutableFloatStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()
    this
        .onGloballyPositioned { coordinates ->
            scrollToPosition = scrollState.value + coordinates.positionInRoot().y
        }
        .onFocusEvent {
            if (it.isFocused) {
                coroutineScope.launch {
                    scrollState.animateScrollTo(scrollToPosition.toInt())
                }
            }
        }
}

@Composable
fun keyboardAsState() : State<Boolean> {
    val keyboardState = remember { mutableStateOf(false) }
    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            keyboardState.value = ViewCompat.getRootWindowInsets(view)?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
    return keyboardState
}