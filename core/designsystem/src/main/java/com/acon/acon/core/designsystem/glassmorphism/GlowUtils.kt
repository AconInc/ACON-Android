package com.acon.acon.core.designsystem.glassmorphism

import android.graphics.Paint
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb

fun Modifier.glowBackground(
    glowColor: Color = Color.White,
    glowAlpha: Float = .16f,
    glowRadius: Float = 200f,
): Modifier {
    return this.drawBehind {
        val size = this.size
        drawContext.canvas.nativeCanvas.apply {
            drawRoundRect(
                0f,
                0f,
                size.width,
                size.height,
                20f,
                20f,
                Paint().apply {
                    color = Color.Transparent.toArgb()
                    setShadowLayer(
                        glowRadius,
                        0f,
                        0f,
                        glowColor.copy(alpha = glowAlpha).toArgb()
                    )
                },
            )
        }
    }
}