package com.acon.acon.core.designsystem.component.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconSnackBar(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color = AconTheme.color.Gray8)
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun AconSnackBarPreview() {
    AconSnackBar(
        content = {
            Text(
                text = "기본 스낵바 메시지입니다.",
                color = AconTheme.color.White,
            )
        }
    )
}
