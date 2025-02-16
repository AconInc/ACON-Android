package com.acon.acon.core.designsystem.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

internal object Skeleton {
    val brush: Brush
        @Composable
        get() = getCommonSkeletonBrush()

    val shape: Shape = RoundedCornerShape(4.dp)
}

@Composable
fun Modifier.skeleton(
    brush: Brush = Skeleton.brush,
    shape: Shape = Skeleton.shape
) = this.background(
    brush = brush,
    shape = shape
)

@Composable
internal fun getCommonSkeletonBrush() : Brush {
    val skeletonColors = listOf(
        AconTheme.color.Gray7.copy(alpha = 0.6f),
        AconTheme.color.Gray2.copy(alpha = 0.2f),
        AconTheme.color.Gray7.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    return Brush.linearGradient(
        colors = skeletonColors,
        start = Offset(x = -translateAnim.value, y = -translateAnim.value),
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
}