package com.acon.acon.core.designsystem.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconGoogleLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val throttleTime = 1000L
    var lastClickTime by remember { mutableLongStateOf(0L) }

    AconFilledButton(
        shape = RoundedCornerShape(percent = 50),
        colors = ButtonDefaults.buttonColors(
            containerColor = AconTheme.color.White,
            contentColor = AconTheme.color.Gray500
        ),
        onClick = {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime >= throttleTime) {
                lastClickTime = currentTime
                onClick()
            }
        },
        contentPadding = PaddingValues(vertical = 17.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_google),
                contentDescription = stringResource(R.string.google_login_btn_description),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 24.dp)
            )

            Text(
                text = stringResource(R.string.google_login_btn_content),
                style = AconTheme.typography.Title4,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewAconGoogleLoginButton() {
    AconTheme {
        Box(
            modifier = Modifier.background(AconTheme.color.Black)
        ) {
            AconGoogleLoginButton(
                onClick = {},
                modifier = Modifier,
            )
        }
    }
}