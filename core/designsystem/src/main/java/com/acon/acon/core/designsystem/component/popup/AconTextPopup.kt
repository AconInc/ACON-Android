package com.acon.acon.core.designsystem.component.popup

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconTextPopup(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    shape: Shape = RoundedCornerShape(20.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 60.dp, vertical = 12.dp)
) {
    Box(
        modifier = modifier
            .clip(shape = shape)
            .border(
                width = 1.dp,
                color = AconTheme.color.GlassWhiteDefault,
                shape = shape
            )
            .clickable(onClick = onClick)
            .defaultHazeEffect(
                hazeState = LocalHazeState.current,
                tintColor = AconTheme.color.GlassBlackDefault
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = AconTheme.typography.Title4,
            color = AconTheme.color.Action,
            fontWeight = FontWeight.W400,
            maxLines = 1,
            modifier = Modifier.padding(contentPadding)
        )   // TODO: Auto Resize
    }
}

@Preview
@Composable
private fun AconTextPopupPreview() {
    AconTextPopup(
        text = "지금 위치 기준으로 다시 추천받기",
        modifier = Modifier.fillMaxWidth()
    )
}