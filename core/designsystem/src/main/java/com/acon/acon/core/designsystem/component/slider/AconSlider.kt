package com.acon.acon.core.designsystem.component.slider

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import com.acon.acon.core.designsystem.theme.AconTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AconSlider(
    labels: List<String>,
    sliderIndex: Int,
    onSliderIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: SliderColors = SliderDefaults.colors(
        thumbColor = AconTheme.color.White,
        activeTrackColor = AconTheme.color.Main_org50,
        inactiveTrackColor = AconTheme.color.Gray8
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val spacing = (labels.size.toFloat() / (labels.size - 1)) / labels.size
    val floatPositions = remember {
        buildList {
            repeat(labels.size) {
                add(spacing * it)
            }
        }
    }
    Column {
        Slider(
            modifier = modifier,
            value = floatPositions[sliderIndex],
            onValueChange = {
                onSliderIndexChange(floatPositions.indexOfFirst { position -> position + spacing / 2f >= it })
            },
            colors = colors,
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = interactionSource,
                    colors = colors,
                    enabled = enabled,
                    thumbSize = DpSize(22.dp, 22.dp)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    modifier = Modifier.scale(scaleX = 1f, scaleY = 0.8f),
                    colors = colors,
                    enabled = enabled,
                    sliderState = sliderState,
                    drawStopIndicator = {},
                    drawTick = { _, _ -> },
                    trackInsideCornerSize = 0.dp,
                    thumbTrackGapSize = 0.dp
                )
            },
        )
        CenterBasedSpaceBetweenRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            labels.fastForEach { label ->
                Text(
                    text = label,
                    style = AconTheme.typography.body2_14_reg,
                    color = AconTheme.color.Gray4
                )
            }
        }
    }
}

@Composable
private fun CenterBasedSpaceBetweenRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val containerWidth = constraints.maxWidth

        val spacing = if (measurables.size == 1) 0 else containerWidth / (measurables.size - 1)
        val placeables = measurables.map {
            it.measure(
                Constraints(
                    maxWidth = Constraints.Infinity,
                    maxHeight = Constraints.Infinity
                )
            )
        }
        val containerHeight = placeables.maxOf { it.height }
        layout(containerWidth, containerHeight) {
            placeables.fastForEachIndexed { i, placeable ->
                if (i != measurables.size - 1)
                    placeable.placeRelative(
                        ((spacing * i) - (placeable.width / 2)).coerceAtLeast(0),
                        0
                    )
                else placeable.placeRelative(containerWidth - placeable.width, 0)
            }
        }
    }
}


@Composable
@Preview
private fun AconSliderPreview() {
    AconSlider(
        labels = listOf("5분 이내", "10분", "15분", "20분", "20분 이상"),
        sliderIndex = 3,
        onSliderIndexChange = {}
    )
}