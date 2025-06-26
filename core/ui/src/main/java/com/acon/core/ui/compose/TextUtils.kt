package com.acon.core.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.DpSize

@Composable
fun getTextSizeDp(
    text: String,
    style: TextStyle
): DpSize {

    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    return remember(text, style, density) {
        val result = textMeasurer.measure(text = text, style = style)
        with(density) {
            DpSize(
                width = result.size.width.toDp(),
                height = result.size.height.toDp()
            )
        }
    }
}