package com.acon.acon.core.designsystem.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = AconTheme.color.White,
    containerColor: Color = AconTheme.color.GlassWhiteDefault,
    textStyle: TextStyle = AconTheme.typography.Body1.copy(
        fontWeight = FontWeight.W400
    )
) {

    val displayTextStyle = if (enabled) {
        textStyle.copy(
            color = AconTheme.color.White
        )
    } else {
        textStyle.copy(
            color = AconTheme.color.Gray300
        )
    }

    val displayContainerColor = if (enabled) {
        containerColor
    } else {
        AconTheme.color.GlassWhiteLight
    }

    Row(
        modifier = modifier
            .clip(CircleShape)
            .border(
                shape = CircleShape,
                width = 1.dp,
                color = if (isSelected) contentColor else Color.Transparent
            ).background(displayContainerColor).noRippleClickable {
                if (enabled) onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 9.dp, horizontal = 12.dp),
            text = title,
            style = displayTextStyle,
        )
    }
}

@Preview
@Composable
private fun AconChipPreview() {
    AconChip(
        title = "한식",
        isSelected = false,
        onClick = {}
    )
}

@Preview
@Composable
private fun SelectedAconChipPreview() {
    AconChip(
        title = "한식",
        isSelected = true,
        onClick = {}
    )
}