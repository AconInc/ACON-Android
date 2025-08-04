package com.acon.acon.core.designsystem.component.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme
import kotlinx.coroutines.delay

@Composable
fun CustomToast(
    message: String,
    modifier: Modifier = Modifier,
    durationMillis: Long = 3000,
    onDismiss: () -> Unit = {}
) {
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(durationMillis)
        if (isVisible) {
            isVisible = false
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            AconToastPopup(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                color = AconTheme.color.DimDefault.copy(alpha = 0.8f),
                content = {
                    Text(
                        text = message,
                        color = AconTheme.color.White,
                        style = AconTheme.typography.Body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 13.dp)
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun CustomToastPreview() {
    AconTheme {
        CustomToast(
            message = "message"
        )
    }
}