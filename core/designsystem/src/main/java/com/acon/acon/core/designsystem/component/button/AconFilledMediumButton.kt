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
fun AconFilledMediumButton(
    text: String,
    textStyle: TextStyle,
    enabledBackgroundColor: Color,
    disabledBackgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = AconTheme.color.White,
    isEnabled: Boolean = true,
    cornerRadius: Dp = 4.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
) {
    AconButton(
        backGroundColor = if(isEnabled) enabledBackgroundColor else disabledBackgroundColor,
        modifier = modifier,
        cornerRadius = cornerRadius,
        contentPadding = contentPadding,
        onClick = onClick,
        enabled = isEnabled
    ) {
        Text(
            text = text,
            style = textStyle,
            color = textColor
        )
    }
}

@Preview
@Composable
private fun PreviewAconMediumButton() {
    AconTheme {
        AconFilledMediumButton(
            text = stringResource(R.string.login_btn_preview_content),
            textStyle = AconTheme.typography.head8_16_sb,
            textColor = AconTheme.color.White,
            enabledBackgroundColor = AconTheme.color.Main_org1,
            disabledBackgroundColor =  AconTheme.color.Main_org1,
            isEnabled = true,
            cornerRadius = 4.dp,
            modifier = Modifier,
            contentPadding =PaddingValues(horizontal = 14.dp, vertical = 14.dp),
            onClick = {}
        )
    }
}
