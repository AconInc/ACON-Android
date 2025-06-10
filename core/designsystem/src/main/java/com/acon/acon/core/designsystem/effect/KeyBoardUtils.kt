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