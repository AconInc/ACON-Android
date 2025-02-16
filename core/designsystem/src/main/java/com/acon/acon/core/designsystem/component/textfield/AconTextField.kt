package com.acon.acon.core.designsystem.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconColors
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconTextField(
    status: TextFieldStatus,
    modifier: Modifier = Modifier,
    text: String = "",
    onTextChanged: (String) -> Unit = {},
    placeholder: String = "",
    onFocusChanged: (Boolean) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val backgroundColor = when (status) {
        TextFieldStatus.Inactive -> AconColors.Gray8
        TextFieldStatus.Focused -> AconColors.Gray9
        TextFieldStatus.Active -> AconColors.Gray9
        TextFieldStatus.Error -> AconColors.Gray9
        TextFieldStatus.Disabled -> AconColors.Gray7
    }

    val borderColor = when (status) {
        TextFieldStatus.Inactive -> AconColors.Gray6
        TextFieldStatus.Focused -> AconColors.Gray6
        TextFieldStatus.Active -> AconColors.Error_red1
        TextFieldStatus.Error -> AconColors.Gray6
        TextFieldStatus.Disabled -> AconColors.Gray6
    }

    val textColor = when (status) {
        TextFieldStatus.Inactive -> AconColors.Gray5
        TextFieldStatus.Focused -> AconColors.Gray5
        TextFieldStatus.Active -> AconColors.White
        TextFieldStatus.Error -> AconColors.Gray6
        TextFieldStatus.Disabled -> AconColors.Gray5
    }

    val isEnabled = status != TextFieldStatus.Disabled

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ){
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester = focusRequester)
                .onFocusChanged { focusState ->
                    onFocusChanged(focusState.isFocused)
                },
            value = text,
            onValueChange = onTextChanged,
            enabled = isEnabled,
            textStyle = AconTheme.typography.body2_14_reg.copy(
                color = textColor
            ),
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            decorationBox = { innerTextField ->
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = AconTheme.typography.body2_14_reg,
                            color = textColor,
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))
                    innerTextField()
                }
            },
        )
    }
}

sealed interface TextFieldStatus {
    data object Inactive: TextFieldStatus
    data object Focused: TextFieldStatus
    data object Active: TextFieldStatus
    data object Error: TextFieldStatus
    data object Disabled: TextFieldStatus
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
private fun AconTextFieldPreview(){

    Column(
        modifier = Modifier.fillMaxSize(),
    ){
        AconTextField(
            status = TextFieldStatus.Inactive,
        )
        Spacer(modifier = Modifier.height(30.dp))
        AconTextField(
            status = TextFieldStatus.Focused,
        )
        Spacer(modifier = Modifier.height(30.dp))
        AconTextField(
            status = TextFieldStatus.Active,
        )
        Spacer(modifier = Modifier.height(30.dp))
        AconTextField(
            status = TextFieldStatus.Error,
        )
        Spacer(modifier = Modifier.height(30.dp))
        AconTextField(
            status = TextFieldStatus.Disabled,
        )
    }
}