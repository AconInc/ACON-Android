package com.acon.acon.core.designsystem.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.theme.AconColors
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconSearchBar(
    status: SearchBarStatus,
    modifier: Modifier = Modifier,
    text: String = "",
    placeholder: String = "",
    onTextChanged: (String) -> Unit = {},
    onFocusChanged: (Boolean) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: @Composable () -> Unit = {
        Row {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search",
                tint = AconColors.White
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
    },
    tailingIcon: @Composable () -> Unit = {
        if (status == SearchBarStatus.Active) {
            Icon(
                painter = painterResource(id = R.drawable.ic_dissmiss_circle_gray),
                contentDescription = "Clear",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onTextChanged("") }
            )
        }
    },
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val backgroundColor = when (status) {
        SearchBarStatus.Inactive -> AconColors.Gray8
        SearchBarStatus.Focused -> AconColors.Gray8
        SearchBarStatus.Active -> AconColors.Gray8
    }

    val borderColor = when (status) {
        SearchBarStatus.Inactive -> AconColors.Gray6
        SearchBarStatus.Focused -> AconColors.Gray5
        SearchBarStatus.Active -> AconColors.Gray5
    }

    val textColor = when (status) {
        SearchBarStatus.Inactive -> AconColors.Gray5
        SearchBarStatus.Focused -> AconColors.Gray5
        SearchBarStatus.Active -> AconColors.White
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, borderColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ){
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .onFocusChanged { focusState ->
                    onFocusChanged(focusState.isFocused)
                },
            value = text,
            onValueChange = onTextChanged,
            textStyle = AconTheme.typography.body2_14_reg.copy(
                color = textColor,
                textAlign = TextAlign.Start
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    leadingIcon()

                    Box(
                        modifier = modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ){
                        innerTextField()
                        if (text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = AconTheme.typography.body2_14_reg,
                                color = textColor,
                            )
                        }
                    }
                    tailingIcon()
                }
            },
        )
    }
}

sealed interface SearchBarStatus {
    data object Inactive: SearchBarStatus
    data object Focused: SearchBarStatus
    data object Active: SearchBarStatus
}

@Preview
@Composable
private fun AconSearchBarPreview(){
    Column(
        modifier = Modifier.fillMaxSize(),
    ){
        AconSearchBar(
            status = SearchBarStatus.Inactive,
        )
        Spacer(modifier = Modifier.height(30.dp))
        AconSearchBar(
            status = SearchBarStatus.Focused
        )
        Spacer(modifier = Modifier.height(30.dp))
        AconSearchBar(
            status = SearchBarStatus.Active
        )
    }
}