package com.acon.acon.core.designsystem.component.snackbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconTextSnackBar(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        AconSnackBar(
            modifier = Modifier.fillMaxWidth(),
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = message,
                        style = AconTheme.typography.body1_15_reg,
                        color = AconTheme.color.White
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun PreviewAconTextSnackBar() {
    AconTextSnackBar(message = "토스트메세지가 작성됩니다.", modifier = Modifier)
}

@Preview
@Composable
private fun PreviewAconTextSnackBar1() {
    AconTextSnackBar(message = "2025.01.07 마케팅 수신에 거부하셨습니다.")
}
