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
fun AconFilledLargeButton(
    text: String,
    textStyle: TextStyle,
    enabledBackgroundColor: Color,
    disabledBackgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabledTextColor: Color = AconTheme.color.White,
    disabledTextColor: Color = AconTheme.color.Gray3,
    isEnabled: Boolean = true,
    cornerRadius: Dp = 6.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
) {
    AconButton(
        backGroundColor = if (isEnabled) enabledBackgroundColor else disabledBackgroundColor,
        modifier = modifier,
        cornerRadius = cornerRadius,
        contentPadding = contentPadding,
        onClick = onClick,
        enabled = isEnabled
    ) {
        Text(
            text = text,
            style = textStyle,
            color = if (isEnabled) enabledTextColor else disabledTextColor
        )
    }
}

@Preview
@Composable
private fun PreviewAconLargeButton() {
    AconTheme {
        AconFilledLargeButton(
            text = stringResource(R.string.login_btn_preview_content),
            textStyle = AconTheme.typography.head8_16_sb,
            enabledTextColor = AconTheme.color.White,
            disabledTextColor = AconTheme.color.Gray3,
            enabledBackgroundColor = AconTheme.color.Main_org1,
            disabledBackgroundColor = AconTheme.color.Main_org1,
            isEnabled = true,
            cornerRadius = 6.dp,
            modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            onClick = {}
        )
    }
}