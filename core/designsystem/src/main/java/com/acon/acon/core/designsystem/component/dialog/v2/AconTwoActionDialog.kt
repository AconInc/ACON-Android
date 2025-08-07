package com.acon.acon.core.designsystem.component.dialog.v2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconTwoActionDialog(
    title: String,
    action1: String,
    action2: String,
    onAction1: () -> Unit,
    onAction2: () -> Unit,
    onDismissRequest: () -> Unit,
    isTextAlign: Boolean = false,
    action1Color: Color = AconTheme.color.White,
    action2Color: Color = AconTheme.color.Action,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        securePolicy = SecureFlagPolicy.Inherit,
        usePlatformDefaultWidth = true,
        decorFitsSystemWindows = true
    ),
    content: @Composable () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = AconTheme.color.Gray4545,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = AconTheme.typography.Title4,
                    fontWeight = FontWeight.SemiBold,
                    color = AconTheme.color.White,
                    textAlign = if (isTextAlign) TextAlign.Center else TextAlign.Unspecified,
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    Text(
                        text = action1,
                        style = AconTheme.typography.Title4,
                        fontWeight = FontWeight.W400,
                        color = action1Color,
                        modifier = Modifier
                            .weight(1f)
                            .noRippleClickable {
                                onAction1()
                            }
                            .padding(vertical = 14.dp),
                        textAlign = TextAlign.Center
                    )
                    VerticalDivider(
                        color = AconTheme.color.Light.copy(alpha = 0.3f),
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxHeight()
                    )
                    Text(
                        text = action2,
                        style = AconTheme.typography.Title4,
                        fontWeight = FontWeight.SemiBold,
                        color = action2Color,
                        modifier = Modifier
                            .weight(1f)
                            .noRippleClickable {
                                onAction2()
                            }
                            .padding(vertical = 14.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun AconTwoActionDialogPreview() {
    AconTwoActionDialog(
        title = "Title",
        action1 = "Action 1",
        action2 = "Action 2",
        onDismissRequest = {},
        onAction1 = {},
        onAction2 = {},
    )
}
