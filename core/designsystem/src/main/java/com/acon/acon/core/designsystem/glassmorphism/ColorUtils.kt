package com.acon.acon.core.designsystem.glassmorphism

import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun Modifier.imageGradientLayer(
    startColor: Color = AconTheme.color.Gray900.copy(alpha = 1f),
    endColor: Color = AconTheme.color.Gray900.copy(alpha = 0f),
    @FloatRange(from = 0.0, to = 1.0) ratio: Float = 0.25f
): Modifier {
    return this.then(
        Modifier.drawWithContent {
            drawContent()

            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(startColor, endColor),
                    startY = 0f,
                    endY = size.height * ratio
                )
            )

            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(endColor, startColor),
                    startY = size.height * (1 - ratio),
                    endY = size.height
                )
            )
        }
    )
}