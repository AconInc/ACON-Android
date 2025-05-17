package com.acon.acon.core.designsystem

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

inline fun Modifier.noRippleClickable(enabled: Boolean = true, crossinline onClick: ()->Unit): Modifier = composed {
    clickable(indication = null, enabled = enabled,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(0.16f),
    blur: Dp = 4.dp,
    offsetY: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp
) = this.drawBehind {
    val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
    val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

    val paint = Paint().apply {
        this.color = color
    }

    if (blur.toPx() > 0) {
        paint.asFrameworkPaint().apply {
            maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
    }

    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(offsetX.toPx(), offsetY.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}

fun Modifier.throttleClick(
    throttleTime: Long = 1000L,
    onClick: () -> Unit
): Modifier {
    var lastClickTime = 3000L
    return this.clickable {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= throttleTime) {
            onClick()
            lastClickTime = currentTime
        }
    }
}