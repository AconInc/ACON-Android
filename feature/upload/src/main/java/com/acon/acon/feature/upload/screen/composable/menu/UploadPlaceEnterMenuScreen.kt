package com.acon.acon.feature.upload.screen.composable.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.textfield.v2.AconOutlinedSearchTextField
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.upload.screen.UploadPlaceUiState

@Composable
internal fun UploadPlaceEnterMenuScreen(
    state: UploadPlaceUiState,
    onSearchQueryChanged: (String) -> Unit,
    onUpdateNextPageBtnEnabled: (Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(state.recommendMenu ?: "")) }
    var isSelection by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        snapshotFlow { query }.collect {
            onSearchQueryChanged(it.text)
            onUpdateNextPageBtnEnabled(it.text.isNotBlank())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AconTheme.color.Gray900)
            .padding(horizontal = 16.dp)
            .pointerInput(Unit) {
            detectTapGestures(onTap = {
                keyboardController?.hide()
                focusManager.clearFocus()
            })
        }
    ) {
        Text(
            text = stringResource(R.string.required_field),
            style = AconTheme.typography.Body1,
            color = AconTheme.color.Danger,
            modifier = Modifier.padding(top = 40.dp)
        )

        Text(
            text = stringResource(R.string.upload_place_enter_menu_title),
            style = AconTheme.typography.Headline3,
            color = AconTheme.color.White,
            modifier = Modifier.padding(top = 4.dp, start = 2.dp)
        )

        Spacer(Modifier.height(32.dp))
        AconOutlinedSearchTextField(
            value = query,
            onValueChange = {
                query = it
                isSelection = false
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            placeholder = stringResource(R.string.upload_place_enter_menu_placeholder),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun UploadPlaceEnterMenuScreenPreview() {
    AconTheme {
        UploadPlaceEnterMenuScreen(
            state = UploadPlaceUiState(),
            onSearchQueryChanged = {},
            onUpdateNextPageBtnEnabled = {}
        )
    }
}