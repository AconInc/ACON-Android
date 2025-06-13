package com.acon.acon.feature.spot.screen.component

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun OperationDot(
    isOpen: Boolean,
    modifier: Modifier = Modifier
) {
    val dotColor = if (isOpen) AconTheme.color.Success else AconTheme.color.Gray500

    Canvas(modifier) {
        drawContext.canvas.nativeCanvas.apply {
            drawCircle(
                center.x, center.y, 20f, Paint().apply {
                    color = Color.Transparent.toArgb()
                    setShadowLayer(
                        20f,
                        0f,
                        0f,
                        dotColor.copy(alpha = 1f).toArgb()
                    )
                }
            )
        }
        drawCircle(
            color = dotColor,
            radius = 4.dp.toPx()
        )
    }
}