package com.acon.acon.feature.profile.composable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.designsystem.theme.AconTheme.color
import com.acon.acon.feature.profile.composable.type.FocusType

@Composable
fun ProfileTextField(
    status: TextFieldStatus,
    focusType: FocusType,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    value: TextFieldValue = TextFieldValue(),
    placeholder: String = "",
    isTyping: Boolean = false,
    onTextChanged: (TextFieldValue) -> Unit = {},
    onFocusChanged: (Boolean, FocusType) -> Unit = { _, _ -> },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onClick: () -> Unit = {},
) {
    val backgroundColor = when (status) {
        TextFieldStatus.Empty -> color.Gray9
        TextFieldStatus.Inactive -> color.Gray9
        TextFieldStatus.Focused -> color.Gray9
        TextFieldStatus.Active -> color.Gray9
        TextFieldStatus.Error -> color.Gray9
        TextFieldStatus.Disabled -> color.Gray7
    }

    val borderColor = when (status) {
        TextFieldStatus.Empty -> color.Gray6
        TextFieldStatus.Inactive -> color.Gray6
        TextFieldStatus.Focused -> color.Gray6
        TextFieldStatus.Active -> color.Gray6
        TextFieldStatus.Error -> color.Error_red1
        TextFieldStatus.Disabled -> color.Gray6
    }

    val textColor = when (status) {
        TextFieldStatus.Empty -> color.White
        TextFieldStatus.Inactive -> color.White
        TextFieldStatus.Focused -> color.White
        TextFieldStatus.Active -> color.White
        TextFieldStatus.Error -> color.White
        TextFieldStatus.Disabled -> color.White
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = RoundedCornerShape(4.dp))
            .border(1.dp, borderColor, shape = RoundedCornerShape(4.dp))
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
                onValueChange = onTextChanged,
                maxLines = 1,
                cursorBrush = SolidColor(color.Success_blue1),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        onFocusChanged(focusState.isFocused, focusType)
                    },
                textStyle = AconTheme.typography.body2_14_reg.copy(
                    color = textColor
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
                                style = AconTheme.typography.body2_14_reg,
                                color = color.Gray3,
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
                        color = color.Gray6
                    )
                } else {
                    Icon(
                        modifier = Modifier
                            .clickable { onTextChanged(TextFieldValue()) }
                            .size(20.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_dissmiss_circle_gray),
                        contentDescription = "Clear text",
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

sealed interface TextFieldStatus {
    data object Empty : TextFieldStatus
    data object Inactive : TextFieldStatus
    data object Focused : TextFieldStatus
    data object Active : TextFieldStatus
    data object Error : TextFieldStatus
    data object Disabled : TextFieldStatus
}

fun Modifier.addFocusCleaner(focusManager: FocusManager, doOnClear: () -> Unit = {}): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            doOnClear()
            focusManager.clearFocus()
        })
    }
}

@Preview
@Composable
private fun ProfileTextFieldPreview() {

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        ProfileTextField(
            status = TextFieldStatus.Inactive,
            focusRequester = FocusRequester(),
            focusType = FocusType.Nickname,
        )
        Spacer(modifier = Modifier.height(30.dp))
        ProfileTextField(
            status = TextFieldStatus.Focused,
            focusRequester = FocusRequester(),
            focusType = FocusType.Nickname,
        )
        Spacer(modifier = Modifier.height(30.dp))
        ProfileTextField(
            status = TextFieldStatus.Active,
            focusRequester = FocusRequester(),
            focusType = FocusType.Nickname,
        )
        Spacer(modifier = Modifier.height(30.dp))
        ProfileTextField(
            status = TextFieldStatus.Error,
            focusRequester = FocusRequester(),
            focusType = FocusType.Nickname,
        )
        Spacer(modifier = Modifier.height(30.dp))
        ProfileTextField(
            status = TextFieldStatus.Disabled,
            focusRequester = FocusRequester(),
            focusType = FocusType.Nickname,
        )
    }
}