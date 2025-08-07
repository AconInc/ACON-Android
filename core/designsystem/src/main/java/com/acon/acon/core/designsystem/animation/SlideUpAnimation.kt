package com.acon.acon.core.designsystem.animation

import android.annotation.SuppressLint
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import kotlinx.coroutines.delay

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun Modifier.slideUpAnimation(
    order: Int = 1,
    delay: Int = 0,
    duration: Int = 500,
    easing: Easing = LinearOutSlowInEasing,
    yOffset: Dp = 100.dp,
    titleDelay: Int = 300,
    contentDelay: Int = 600,
    hasCaption: Boolean = false,
    onAnimationEnded: () -> Unit = {}
): Modifier = composed {
    val animationDelay = remember(order, hasCaption) {
        when (order) {
            1 -> delay
            2 -> delay + titleDelay
            3 -> if (hasCaption) delay + titleDelay * 2 else 0
            4 -> if (hasCaption) {
                delay + titleDelay * 2 + contentDelay
            } else {
                delay + titleDelay + contentDelay
            }
            else -> delay
        }
    }

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(animationDelay.toLong())
        visible = true
    }

    val offsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else yOffset,
        animationSpec = tween(
            durationMillis = duration,
            easing = easing
        ),
        label = stringResource(R.string.slide_up_offset)
    )

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(
            durationMillis = duration,
            easing = easing
        ),
        label = stringResource(R.string.slide_up_alpha)
    )

    LaunchedEffect(offsetY) {
        if (offsetY == 0.dp) {
            onAnimationEnded()
        }
    }

    Modifier
        .offset(y = offsetY)
        .alpha(alpha)
}