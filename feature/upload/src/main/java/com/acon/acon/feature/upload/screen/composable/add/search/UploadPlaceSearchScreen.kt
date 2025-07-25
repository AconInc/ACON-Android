package com.acon.acon.feature.upload.screen.composable.add.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.textfield.v2.AconSearchTextField
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
internal fun UploadPlaceSearchScreen(

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

        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.upload_place_search_title),
            style = AconTheme.typography.Headline3,
            color = AconTheme.color.White
        )

        Spacer(Modifier.height(20.dp))
        AconSearchTextField(
            value = query,
            onValueChange = {
                query = it
                isSelection = false
            },
            placeholder = stringResource(R.string.upload_place_search_placeholder),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun UploadPlaceSearchScreenPreview() {
    AconTheme {
        UploadPlaceSearchScreen()
    }
}