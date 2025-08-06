package com.acon.acon.feature.upload.screen.composable.add.place

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.model.type.CafeFeatureType
import com.acon.acon.core.model.type.RestaurantFeatureType
import com.acon.acon.core.model.type.SpotType
import com.acon.acon.feature.upload.screen.UploadPlaceUiState
import com.acon.acon.feature.upload.screen.composable.add.UploadPlaceSelectItem
import com.acon.acon.feature.upload.screen.composable.type.getNameResId
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun UploadSelectPlaceDetailScreen(
    state: UploadPlaceUiState,
    onUpdateCafeOptionType: (CafeFeatureType.CafeType) -> Unit,
    onUpdateRestaurantType: (RestaurantFeatureType.RestaurantType) -> Unit,
    onUpdateNextPageBtnEnabled: (Boolean) -> Unit
) {
    val allRestaurantTypes = remember {
        persistentListOf(*RestaurantFeatureType.RestaurantType.entries.toTypedArray())
    }

    val isNextPageBtnEnabled by remember(state) {
        derivedStateOf {
            when (state.selectedSpotType) {
                SpotType.RESTAURANT -> state.selectedRestaurantTypes.isNotEmpty()
                SpotType.CAFE -> state.selectedCafeOption != null
                else -> false
            }
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
        when (state.selectedSpotType) {
            SpotType.RESTAURANT  -> {
                Text(
                    text = stringResource(R.string.required_field),
                    style = AconTheme.typography.Body1,
                    color = AconTheme.color.Danger,
                    modifier = Modifier.padding(top = 40.dp)
                )

                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.upload_place_select_restaurant_title),
                    style = AconTheme.typography.Headline3,
                    color = AconTheme.color.White
                )

                Spacer(Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.upload_place_select_cafe_sub_title),
                    style = AconTheme.typography.Title5,
                    color = AconTheme.color.Gray500,
                    fontWeight = FontWeight.Normal
                )

                Spacer(Modifier.height(32.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    allRestaurantTypes.chunked(2).fastForEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            pair.forEach { restaurantType ->
                                UploadPlaceSelectItem(
                                    title = stringResource(id = restaurantType.getNameResId()),
                                    modifier = Modifier.weight(1f),
                                    isSelected = state.selectedRestaurantTypes.contains(restaurantType),
                                    onClickUploadPlaceSelectItem = {
                                        onUpdateRestaurantType(restaurantType)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            SpotType.CAFE -> {
                Text(
                    text = stringResource(R.string.required_field),
                    style = AconTheme.typography.Body1,
                    color = AconTheme.color.Danger,
                    modifier = Modifier.padding(top = 40.dp)
                )

                Spacer(Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.upload_place_select_restaurant_cafe),
                    style = AconTheme.typography.Headline3,
                    color = AconTheme.color.White
                )

                Spacer(Modifier.height(32.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    UploadPlaceSelectItem(
                        title = stringResource(CafeFeatureType.CafeType.GOOD_FOR_WORK.getNameResId()),
                        isSelected = state.selectedCafeOption == CafeFeatureType.CafeType.GOOD_FOR_WORK,
                        onClickUploadPlaceSelectItem = {
                            onUpdateCafeOptionType(CafeFeatureType.CafeType.GOOD_FOR_WORK)
                        }
                    )
                    UploadPlaceSelectItem(
                        title = stringResource(CafeFeatureType.CafeType.NOT_GOOD_FOR_WORK.getNameResId()),
                        isSelected = state.selectedCafeOption == CafeFeatureType.CafeType.NOT_GOOD_FOR_WORK,
                        onClickUploadPlaceSelectItem = {
                            onUpdateCafeOptionType(CafeFeatureType.CafeType.NOT_GOOD_FOR_WORK)
                        }
                    )
                }
            }

            null -> {}
        }
    }
}

@Preview
@Composable
private fun UploadSelectPlaceDetailScreenPreview() {
    AconTheme {
        UploadSelectPlaceDetailScreen(
            state = UploadPlaceUiState(),
            onUpdateCafeOptionType = {},
            onUpdateRestaurantType = {},
            onUpdateNextPageBtnEnabled = {}
        )
    }
}