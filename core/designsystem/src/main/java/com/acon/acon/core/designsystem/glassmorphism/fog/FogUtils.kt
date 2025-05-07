package com.acon.acon.core.designsystem.glassmorphism.fog

import android.content.Context
import android.graphics.Paint
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.palette.graphics.Palette
import com.acon.acon.core.designsystem.image.toBitmap

fun Modifier.fogBackground(
    glowColor: Color,
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

/**
 * 이미지의 Overlay Color 추출
 * @receiver 이미지 URL
 * @param context Context
 * @return Dominant Color
 */
suspend fun String.getOverlayColor(
    context: Context
): Color {
    val cached = imageOverlayColorCache[this]
    if (cached != null)
        return cached

    val bitmap = this.toBitmap(context = context) ?: return Color.Transparent

    val palette = Palette.from(bitmap).generate()

    val dominantColorInt = palette.getDominantColor(Color.Transparent.toArgb())
    val overlayColorInt = palette.getDarkMutedColor(dominantColorInt)
    val color = Color(overlayColorInt)

    imageOverlayColorCache.put(this, color)
    return color
}