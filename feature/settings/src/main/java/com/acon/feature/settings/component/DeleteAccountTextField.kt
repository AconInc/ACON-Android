package com.acon.feature.settings.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.core.designsystem.noRippleClickable
import com.acon.core.designsystem.theme.AconTheme
import com.acon.feature.settings.R

@Composable
fun DeleteAccountTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    maxLength: Int = 50,
    onClickResetText: () -> Unit = {},
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .background(color = AconTheme.color.Gray9),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(4.dp))
                .background(
                    shape = RoundedCornerShape(4.dp),
                    color = AconTheme.color.Gray9
                )
                .border(
                    shape = RoundedCornerShape(4.dp),
                    width = 1.dp,
                    color = AconTheme.color.Gray6
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = { inputText ->
                        if(inputText.length <= maxLength) {
                            onValueChange(inputText)
                        }
                    },
                    textStyle = AconTheme.typography.subtitle1_16_med.copy(
                        color = AconTheme.color.White
                    ),
                    cursorBrush = SolidColor(AconTheme.color.White),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if(value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = AconTheme.typography.subtitle1_16_med,
                                    color = AconTheme.color.Gray5
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                Image(
                    imageVector = ImageVector.vectorResource(id = com.acon.core.designsystem.R.drawable.ic_dissmiss_circle_gray),
                    contentDescription = stringResource(R.string.reason_reset_btn_content_description),
                    modifier = Modifier
                        .noRippleClickable { onClickResetText() }
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
                style = AconTheme.typography.subtitle2_14_med,
                color = AconTheme.color.White
            )
            Text(
                text = stringResource(R.string.reason_max_length),
                style = AconTheme.typography.subtitle2_14_med,
                color = AconTheme.color.Gray5,
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
                .background(AconTheme.color.Gray9)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            DeleteAccountTextField(
                value = "탈퇴하려는 이유를 작성하다가 다음 줄로 넘어가게 된다면 이런 식으로 보입니다 딱 오십자임다",
                onValueChange = {},
                placeholder = stringResource(R.string.reason_other_placeholder),
            )
        }
    }
}
