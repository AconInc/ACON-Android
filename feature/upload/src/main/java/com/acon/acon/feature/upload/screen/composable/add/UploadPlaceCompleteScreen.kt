package com.acon.acon.feature.upload.screen.composable.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
internal fun UploadPlaceCompleteScreen(

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AconTheme.color.Gray900),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.upload_place_complete_title),
            style = AconTheme.typography.Headline3,
            color = AconTheme.color.White,
            modifier = Modifier
                .padding(top = 50.dp)
        )

        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.upload_place_complete_sub_title),
            style = AconTheme.typography.Title5,
            color = AconTheme.color.Gray500,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(2.dp)
        )

        // TODO - 로티 삽입 예정
    }
}

@Preview
@Composable
private fun UploadPlaceCompleteScreenPreview() {
    AconTheme {
        UploadPlaceCompleteScreen()
    }
}