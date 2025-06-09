package com.acon.acon.core.designsystem.component.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconToastPopup(
    modifier: Modifier = Modifier,
    minHeight: Dp = 40.dp,
    color: Color = AconTheme.color.Gray4545,
    shape: Shape = RoundedCornerShape(50),
    content: @Composable() (RowScope.() -> Unit)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .background(
                color = color,
                shape = shape
            )
            .border(
                width = 1.dp,
                color = AconTheme.color.GlassWhiteDefault,
                shape = shape
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Preview
@Composable
private fun AconToastPopupSquarePreview() {
    AconTheme {
        AconToastPopup(
            shape = RoundedCornerShape(8.dp),
            content = {
                Text(
                    text = "인증 지역은 프로필에서 수정 가능합니다.",
                    color = AconTheme.color.White,
                    style = AconTheme.typography.Body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        )
    }
}

@Preview
@Composable
private fun AconToastPopupPreview() {
    AconTheme {
        AconToastPopup(
            content = {
                Text(
                    text = "인증 지역은 프로필에서 수정 가능합니다.",
                    color = AconTheme.color.White,
                    style = AconTheme.typography.Body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        )
    }
}