package com.acon.acon.feature.upload.screen.composable.add.place

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.model.type.SpotType
import com.acon.acon.feature.upload.screen.UploadPlaceUiState
import com.acon.acon.feature.upload.screen.composable.add.UploadPlaceSelectItem
import com.acon.acon.feature.upload.screen.composable.type.getNameResId

@Composable
internal fun UploadSelectPlaceScreen(
    state: UploadPlaceUiState,
    onSelectSpotType: (SpotType) -> Unit,
    onUpdateNextPageBtnEnabled: (Boolean) -> Unit
) {
    LaunchedEffect(state.selectedSpotType) {
        onUpdateNextPageBtnEnabled(state.selectedSpotType != null)
    }

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
            text = stringResource(R.string.upload_place_select_place_title),
            style = AconTheme.typography.Headline3,
            color = AconTheme.color.White
        )

        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.upload_place_select_place_sub_title),
            style = AconTheme.typography.Title5,
            color = AconTheme.color.Gray500,
            fontWeight = FontWeight.Normal
        )

        Spacer(Modifier.height(32.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UploadPlaceSelectItem(
                title = stringResource(SpotType.RESTAURANT.getNameResId()),
                isSelected = state.selectedSpotType == SpotType.RESTAURANT,
                onClickUploadPlaceSelectItem = {
                    onSelectSpotType(SpotType.RESTAURANT)
                }
            )
            UploadPlaceSelectItem(
                title = stringResource(SpotType.CAFE.getNameResId()),
                isSelected = state.selectedSpotType == SpotType.CAFE,
                onClickUploadPlaceSelectItem = {
                    onSelectSpotType(SpotType.CAFE)
                }
            )
        }
    }
}

@Preview
@Composable
private fun UploadSelectPlaceScreenPreview() {
    AconTheme {
        UploadSelectPlaceScreen(
            state = UploadPlaceUiState(),
            onSelectSpotType = {},
            onUpdateNextPageBtnEnabled = {},
        )
    }
}