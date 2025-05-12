package com.acon.acon.feature.upload.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.upload.R

@Composable
fun AconSearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .noRippleClickable(onClick = onClick)
            .background(
                shape = RoundedCornerShape(8.dp),
                color = AconTheme.color.Gray8
            )
            .border(
                shape = RoundedCornerShape(8.dp),
                width = 1.dp,
                color = AconTheme.color.Gray6
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = com.acon.acon.core.designsystem.R.drawable.ic_search),
                contentDescription = null,
            )

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = AconTheme.typography.body2_14_reg.copy(
                    color = AconTheme.color.White
                ),
                maxLines = 1,
                cursorBrush = SolidColor(AconTheme.color.White),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .focusRequester(focusRequester),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = AconTheme.typography.body2_14_reg,
                                color = AconTheme.color.Gray5
                            )
                        }
                        innerTextField()
                    }
                }
            )

            if (value.isNotEmpty()) {
                Image(
                    imageVector = ImageVector.vectorResource(id = com.acon.acon.core.designsystem.R.drawable.ic_dissmiss_circle_gray),
                    contentDescription = "Clear",
                    modifier = Modifier
                        .clickable { onValueChange("") }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AconSearchTextFieldPreview() {
    val focusRequester = remember { FocusRequester() }
    var text by remember { mutableStateOf("") }

    AconTheme {
        AconSearchTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = "장소를 검색해보세요",
            focusRequester = focusRequester,
            modifier = Modifier.padding(16.dp)
        )
    }
}
