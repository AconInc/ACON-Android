package com.acon.acon.feature.upload.screen.composable.add.price

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.model.type.PriceOptionType
import com.acon.acon.feature.upload.screen.UploadPlaceUiState
import com.acon.acon.feature.upload.screen.composable.add.UploadPlaceSelectItem
import com.acon.acon.feature.upload.screen.composable.type.getNameResId

@Composable
internal fun UploadSelectPriceScreen(
    state: UploadPlaceUiState,
    onUpdatePriceOptionType: (PriceOptionType) -> Unit,
    onUpdateNextPageBtnEnabled: (Boolean) -> Unit
) {
    val isNextPageBtnEnabled by remember(state) {
        derivedStateOf {
            state.selectedPriceOption != null
        }
    }

    LaunchedEffect(isNextPageBtnEnabled) {
        onUpdateNextPageBtnEnabled(isNextPageBtnEnabled)
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
            text = stringResource(R.string.upload_place_select_price_title),
            style = AconTheme.typography.Headline3,
            color = AconTheme.color.White,
            modifier = Modifier.padding(2.dp)
        )

        Spacer(Modifier.height(32.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UploadPlaceSelectItem(
                title = stringResource(PriceOptionType.VALUE_FOR_MONEY.getNameResId()),
                isSelected = state.selectedPriceOption == PriceOptionType.VALUE_FOR_MONEY,
                onClickUploadPlaceSelectItem = {
                    onUpdatePriceOptionType(PriceOptionType.VALUE_FOR_MONEY)
                }
            )
            UploadPlaceSelectItem(
                title = stringResource(PriceOptionType.AVERAGE_VALUE.getNameResId()),
                isSelected = state.selectedPriceOption == PriceOptionType.AVERAGE_VALUE,
                onClickUploadPlaceSelectItem = {
                    onUpdatePriceOptionType(PriceOptionType.AVERAGE_VALUE)
                }
            )
            UploadPlaceSelectItem(
                title = stringResource(PriceOptionType.LOW_VALUE.getNameResId()),
                isSelected = state.selectedPriceOption == PriceOptionType.LOW_VALUE,
                onClickUploadPlaceSelectItem = {
                    onUpdatePriceOptionType(PriceOptionType.LOW_VALUE)
                }
            )
        }
    }
}

@Preview
@Composable
private fun UploadSelectPriceScreenPreview(

) {
    AconTheme {
        UploadSelectPriceScreen(
            state = UploadPlaceUiState(),
            onUpdatePriceOptionType = {},
            onUpdateNextPageBtnEnabled = {}
        )
    }
}