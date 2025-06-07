package com.acon.acon.core.designsystem.component.radiobutton

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AconRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: AconRadioButtonColors = AconRadioButtonColors.DeleteAccountRadioButtonColors()
) {
    val containerColor =
        if (selected) colors.selectedContainerColor else colors.unselectedContainerColor
    val contentColor = if (selected) colors.selectedContentColor else colors.unselectedContentColor
    val borderColor = colors.borderColor

    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .border(
                shape = CircleShape,
                width = 1.dp,
                color = borderColor
            )
            .background(containerColor)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
                .clip(CircleShape)
                .background(contentColor)
        )
    }
}

@Preview
@Composable
private fun AconRadioButtonPreview() {
    AconRadioButton(
        selected = false,
        onClick = {}
    )
}

@Preview
@Composable
private fun AconSelectedRadioButtonPreview() {
    AconRadioButton(
        selected = true,
        onClick = {}
    )
}

@Preview
@Composable
private fun AconDisabledRadioButtonPreview() {
    AconRadioButton(
        selected = false,
        onClick = {}
    )
}