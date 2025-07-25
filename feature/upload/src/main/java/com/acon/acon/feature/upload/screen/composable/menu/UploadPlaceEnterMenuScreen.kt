package com.acon.acon.feature.upload.screen.composable.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.textfield.v2.AconSearchTextField
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
internal fun UploadPlaceEnterMenuScreen(

) {

    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    var isSelection by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AconTheme.color.Gray900)
            .padding(horizontal = 16.dp)
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

        Spacer(Modifier.height(20.dp))
        AconSearchTextField(
            value = query,
            onValueChange = {
                query = it
                isSelection = false
            },
            placeholder = stringResource(R.string.upload_place_enter_menu_placeholder),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun UploadPlaceEnterMenuScreenPreview() {
    AconTheme {
        UploadPlaceEnterMenuScreen()
    }
}