package com.acon.acon.core.designsystem.component.dialog.v2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconDefaultDialog(
    title: String,
    action: String,
    onAction: () -> Unit,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        securePolicy = SecureFlagPolicy.Inherit,
        usePlatformDefaultWidth = true,
        decorFitsSystemWindows = true
    ),
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = AconTheme.color.GlassWhiteDefault.copy(alpha = .4f),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().defaultHazeEffect(
                    hazeState = LocalHazeState.current,
                    tintColor = AconTheme.color.GlassWhiteDefault,
                ), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = AconTheme.typography.Title4,
                    fontWeight = FontWeight.SemiBold,
                    color = AconTheme.color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 22.dp)
                        .padding(horizontal = 16.dp)
                )
                content()
                HorizontalDivider(
                    color = AconTheme.color.Light.copy(alpha = 0.3f),
                    thickness = 1.dp,
                    modifier = Modifier
                )
                Text(
                    text = action,
                    style = AconTheme.typography.Title4,
                    color = AconTheme.color.Action,
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClickable {
                            onAction()
                        }
                        .padding(vertical = 10.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@Preview
private fun AconDefaultDialogPreview() {
    AconDefaultDialog(
        title = "Title",
        action = "Action",
        onDismissRequest = {},
        onAction = {}
    ) {}
}

@Composable
@Preview
private fun AconDefaultDialogWithContentPreview() {
    AconDefaultDialog(
        title = "Title",
        action = "Action",
        onDismissRequest = {},
        onAction = {}
    ) {
        Text(
            text = "Content",
            style = AconTheme.typography.Body1,
            color = AconTheme.color.Gray200,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp)
        )
    }
}