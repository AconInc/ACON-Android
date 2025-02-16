package com.acon.acon.core.designsystem.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.theme.AconTheme
@Composable
fun AconGoogleLoginButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    backgroundColor: Color = AconTheme.color.White,
    borderColor: Color = AconTheme.color.Gray1,
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = 6.dp,
    textColor: Color = AconTheme.color.Black,
    textStyle: TextStyle = AconTheme.typography.subtitle2_14_med,
) {
    AconButton(
        backGroundColor = backgroundColor,
        borderColor = borderColor,
        modifier = modifier
            .fillMaxWidth(),
        borderWidth = borderWidth,
        cornerRadius = cornerRadius,
        contentPadding = PaddingValues(vertical = 12.dp),
        onClick = onClick
    ) {
        Row(
           verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_google),
                contentDescription = stringResource(R.string.google_login_btn_description)
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = stringResource(R.string.google_login_btn_content),
                style = textStyle,
                color = textColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }
    }

}

@Preview
@Composable
private fun PreviewAconGoogleLoginButton() {
    AconTheme {
        AconGoogleLoginButton(
            modifier = Modifier,
            textColor = AconTheme.color.Black,
            backgroundColor = AconTheme.color.White,
            onClick = {},
        )
    }
}