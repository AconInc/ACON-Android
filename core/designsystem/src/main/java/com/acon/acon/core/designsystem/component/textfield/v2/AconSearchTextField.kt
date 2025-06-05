package com.acon.acon.core.designsystem.component.textfield.v2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconSearchTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    minHeight: Dp = 38.dp,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = AconTheme.typography.Title4.copy(fontWeight = FontWeight.Normal, color = AconTheme.color.White),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val result = textMeasurer.measure(text = "▪", style = textStyle)

    val height = remember {
        with(density) {
            result.size.height.toDp()
        }
    }
    AconFilledTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.heightIn(min = minHeight),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        maxLines = 1,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
    ) { innerTextField ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                    contentDescription = stringResource(R.string.search_content_description),
                    tint = AconTheme.color.Gray50,
                    modifier = Modifier.width(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier.height(height),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = textStyle.copy(color = AconTheme.color.Gray500),
                        )
                    }
                    innerTextField()
                }
            }
            if (value.text.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_clear),
                    contentDescription = stringResource(R.string.clear_search_content_description),
                    tint = AconTheme.color.Gray50,
                    modifier = Modifier
                        .width(18.dp)
                        .noRippleClickable { onValueChange(TextFieldValue("")) }
                )
            }
        }
    }
}

@Composable
@Preview
private fun AconSearchTextFieldPlaceholderPreview() {
    AconTheme {
        AconSearchTextField(
            value = TextFieldValue(""),
            onValueChange = { },
            placeholder = "장소를 입력해주세요",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun AconSearchTextFieldPreview() {
    AconTheme {
        AconSearchTextField(
            value = TextFieldValue("버거킹"),
            onValueChange = { },
            placeholder = "장소를 입력해주세요",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}