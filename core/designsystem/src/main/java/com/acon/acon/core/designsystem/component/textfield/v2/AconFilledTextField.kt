package com.acon.acon.core.designsystem.component.textfield.v2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.glassmorphism.LocalHazeState
import com.acon.acon.core.designsystem.glassmorphism.defaultHazeEffect
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconFilledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = AconTheme.typography.Title4.copy(fontWeight = FontWeight.Normal, color = AconTheme.color.White),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit = @Composable { innerTextField -> innerTextField() }
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .background(
                shape = RoundedCornerShape(10.dp),
                color = AconTheme.color.GlassWhiteDefault
            ).padding(horizontal = 8.dp, vertical = 10.dp)
            .defaultHazeEffect(
                hazeState = LocalHazeState.current,
                tintColor = AconTheme.color.GlassWhiteDefault,
            ),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = false,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        cursorBrush = SolidColor(AconTheme.color.Action)
    ) { innerTextField ->
        decorationBox(innerTextField)
    }
}

@Composable
@Preview
private fun AconDefaultTextFieldPreview(
) {
    AconFilledTextField(
        value = "Search Text",
        onValueChange = { },
        modifier = Modifier.fillMaxWidth(),
    ) { innerTextField ->
        innerTextField()
    }
}