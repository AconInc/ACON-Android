package com.acon.acon.feature.profile.composable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.profile.composable.type.FocusType
import com.acon.acon.feature.profile.composable.type.TextFieldStatus

@Composable
internal fun ProfileTextField(
    status: TextFieldStatus,
    focusType: FocusType,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    value: TextFieldValue = TextFieldValue(),
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String,
    isTyping: Boolean = false,
    onFocusChanged: (Boolean, FocusType) -> Unit = { _, _ -> },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(8.dp),
                color = AconTheme.color.Gray900
            )
            .border(
                width = 1.dp,
                color = AconTheme.color.GlassWhiteDefault,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                maxLines = 1,
                cursorBrush = SolidColor(AconTheme.color.Action),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        onFocusChanged(focusState.isFocused, focusType)
                    },
                textStyle = AconTheme.typography.Body1.copy(
                    color = AconTheme.color.White
                ),
                visualTransformation = visualTransformation,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.text.isEmpty() && status != TextFieldStatus.Focused) {
                            Text(
                                text = placeholder,
                                style = AconTheme.typography.Body1,
                                color = AconTheme.color.Gray500
                            )
                        }
                        innerTextField()
                    }
                }
            )

            if (value.text.isNotEmpty()) {
                if (isTyping) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = AconTheme.color.Gray6
                    )
                } else {
                    Icon(
                        modifier = Modifier
                            .clickable { onValueChange(TextFieldValue()) }
                            .size(20.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_clear),
                        contentDescription = stringResource(R.string.clear_search_content_description),
                        tint = AconTheme.color.Gray50
                    )
                }
            }
        }
    }
}

fun Modifier.addFocusCleaner(focusManager: FocusManager, doOnClear: () -> Unit = {}): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(
            onTap = {
                doOnClear()
                focusManager.clearFocus()
            }
        )
    }
}

@Preview
@Composable
private fun ProfileTextFieldPreview() {
    AconTheme {
        ProfileTextField(
            status = TextFieldStatus.Inactive,
            focusRequester = FocusRequester(),
            focusType = FocusType.Nickname,
            onValueChange = {},
            placeholder = ""
        )
    }
}