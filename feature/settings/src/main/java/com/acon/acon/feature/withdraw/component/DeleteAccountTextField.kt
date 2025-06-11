package com.acon.acon.feature.withdraw.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun DeleteAccountTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    maxLength: Int = 50,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val lineHeight = AconTheme.typography.Body1.lineHeight
    val density = LocalDensity.current
    val threeLineHeight = with(density) { (lineHeight * 3).toDp() }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    shape = RoundedCornerShape(8.dp),
                    width = 1.dp,
                    color = AconTheme.color.GlassWhiteDefault
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .height(threeLineHeight),
                verticalAlignment = Alignment.Top
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = { inputText ->
                        if (inputText.length <= maxLength) {
                            onValueChange(inputText)
                        }
                    },
                    textStyle = AconTheme.typography.Body1.copy(
                        color = AconTheme.color.White
                    ),
                    cursorBrush = SolidColor(AconTheme.color.White),
                    modifier = Modifier
                        .focusRequester(focusRequester),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = AconTheme.typography.Body1,
                                    color = AconTheme.color.Gray500
                                )
                            }
                            innerTextField()
                        }
                    },
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = value.length.toString(),
                style = AconTheme.typography.Caption1,
                color = AconTheme.color.White
            )
            Text(
                text = stringResource(R.string.reason_max_length),
                style = AconTheme.typography.Caption1,
                color = AconTheme.color.Gray500,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteAccountTextFiledPreview() {
    AconTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AconTheme.color.Gray900)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            DeleteAccountTextField(
                value = "탈퇴하려는 이유를 작성하다가 다음 줄로 넘어가게 된다면 이런 식으로 보입니다 딱 오십자임다",
                onValueChange = {},
                focusRequester = FocusRequester(),
                placeholder = stringResource(R.string.reason_other_placeholder),
            )
        }
    }
}