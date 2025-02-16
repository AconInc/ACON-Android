package com.acon.acon.core.designsystem.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.theme.AconTheme
@Composable
fun AconOutlinedLargeButton(
    text: String,
    enabledBorderColor: Color,
    enabledBackgroundColor: Color,
    disabledBorderColor: Color,
    disabledBackgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color? = null,
    borderColor: Color? = null,
    backgroundColor: Color? = null,
    textStyle: TextStyle = AconTheme.typography.head8_16_sb,
    enabledTextColor: Color = AconTheme.color.White,
    disabledTextColor: Color = AconTheme.color.Gray3,
    isEnabled: Boolean = true,
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = 6.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
) {
    val effectiveBackgroundColor = backgroundColor ?: if (isEnabled) enabledBackgroundColor else disabledBackgroundColor
    val effectiveBorderColor = borderColor ?: if (isEnabled) enabledBorderColor else disabledBorderColor
    val effectiveTextColor = textColor ?: if (isEnabled) enabledTextColor else disabledTextColor

    AconButton (
        backGroundColor = effectiveBackgroundColor,
        borderColor = effectiveBorderColor,
        borderWidth = borderWidth,
        modifier = modifier,
        cornerRadius = cornerRadius,
        contentPadding = contentPadding,
        onClick = onClick,
        enabled = isEnabled
    ) {
        Text(
            text = text,
            style = textStyle,
            color = effectiveTextColor
        )
    }
}

@Preview
@Composable
private fun PreviewAconOutlinedButton() {
    AconTheme {
        AconOutlinedLargeButton(
            text = stringResource(R.string.login_btn_preview_content),
            textStyle = AconTheme.typography.head8_16_sb,
            enabledBorderColor = AconTheme.color.Gray5,
            enabledBackgroundColor = AconTheme.color.Gray9,
            disabledBorderColor = AconTheme.color.Gray6,
            disabledBackgroundColor =  AconTheme.color.Gray8,
            isEnabled = true,
            borderWidth = 1.dp,
            cornerRadius = 6.dp,
            modifier = Modifier,
            contentPadding =PaddingValues(horizontal = 14.dp, vertical = 14.dp),
            onClick = {}
        )
    }
}