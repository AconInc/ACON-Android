package com.acon.acon.feature.verification.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AddAreaButton(
    text: String,
    @DrawableRes contentImage: Int,
    enabledBorderColor: Color,
    enabledBackgroundColor: Color,
    enabledTextColor: Color,
    disabledBorderColor: Color,
    disabledBackgroundColor: Color,
    disabledTextColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    textStyle: TextStyle = AconTheme.typography.head8_16_sb,
    isEnabled: Boolean = true,
    iconSize: Dp = 20.dp,
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = 4.dp,
    contentPadding: PaddingValues = PaddingValues(vertical = 12.dp)
) {
    val backgroundColor = if (isEnabled) enabledBackgroundColor else disabledBackgroundColor
    val borderColor = if (isEnabled) enabledBorderColor else disabledBorderColor
    val textColor = if (isEnabled) enabledTextColor else disabledTextColor

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(color = backgroundColor)
            .clickable(
                enabled = isEnabled,
                onClick = onClick
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Row {
            Icon(
                imageVector = ImageVector.vectorResource(contentImage),
                contentDescription = "",
                tint = textColor,
                modifier = Modifier
                    .size(iconSize)
            )

            Spacer(Modifier.width(4.dp))
            Text(
                text = text,
                style = textStyle,
                color = textColor
            )
        }

    }
}

@Preview
@Composable
fun AddAreaButtonEnabledPreview() {
    AconTheme{
        AddAreaButton(
            text = "동네 추가하기",
            contentImage = com.acon.acon.core.designsystem.R.drawable.ic_add_16,
            enabledBorderColor = AconTheme.color.Gray5,
            enabledBackgroundColor = AconTheme.color.Gray9,
            enabledTextColor = AconTheme.color.White,
            disabledBorderColor = AconTheme.color.Gray6,
            disabledBackgroundColor = AconTheme.color.Gray7,
            disabledTextColor = AconTheme.color.Gray5,
            onClick = {},
            textStyle = AconTheme.typography.subtitle1_16_med,
            isEnabled = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun AddAreaButtonDisabledPreview() {
    AconTheme{
        AddAreaButton(
            text = "동네 추가하기",
            contentImage = com.acon.acon.core.designsystem.R.drawable.ic_add_16,
            enabledBorderColor = AconTheme.color.Gray5,
            enabledBackgroundColor = AconTheme.color.Gray9,
            enabledTextColor = AconTheme.color.White,
            disabledBorderColor = AconTheme.color.Gray6,
            disabledBackgroundColor = AconTheme.color.Gray7,
            disabledTextColor = AconTheme.color.Gray5,
            onClick = {},
            textStyle = AconTheme.typography.subtitle1_16_med,
            isEnabled = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


