package com.acon.acon.feature.spot.screen.spotlist.composable.bottomsheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.component.chip.AconChipFlowRow
import com.acon.acon.core.designsystem.component.slider.AconSlider
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.type.OptionType
import com.acon.acon.feature.spot.getNameResId
import com.acon.acon.feature.spot.type.AvailableWalkingTimeType
import com.acon.acon.feature.spot.type.RestaurantPriceRangeType
import com.acon.acon.feature.spot.R

@Composable
fun ColumnScope.RestaurantBottomSheetContent(
    selectedRestaurantFeatures: List<OptionType.RestaurantFeatureOptionType>,
    selectedCompanionTypes: List<OptionType.CompanionTypeOptionType>,
    selectedWalkingTime: AvailableWalkingTimeType,
    selectedPriceRange: RestaurantPriceRangeType,
    onRestaurantFeatureChipSelected: (OptionType.RestaurantFeatureOptionType) -> Unit,
    onCompanionTypeChipSelected: (OptionType.CompanionTypeOptionType) -> Unit,
    onWalkingTimeChange: (AvailableWalkingTimeType) -> Unit,
    onPriceRangeChange: (RestaurantPriceRangeType) -> Unit,
) {

    AconChipFlowRow(
        modifier = Modifier.padding(top = 12.dp),
        titles = OptionType.RestaurantFeatureOptionType.entries.map {
            stringResource(it.getNameResId())
        },
        selectedChipIndexes = selectedRestaurantFeatures.map { it.ordinal }.toIntArray(),
        onChipSelected = {
            onRestaurantFeatureChipSelected(OptionType.RestaurantFeatureOptionType.entries[it])
        }
    )

    Text(
        modifier = Modifier.padding(top = 32.dp),
        text = stringResource(R.string.people_together),
        style = AconTheme.typography.head7_18_sb,
        color = AconTheme.color.White,
    )
    AconChipFlowRow(
        modifier = Modifier.padding(top = 12.dp),
        titles = OptionType.CompanionTypeOptionType.entries.map {
            stringResource(it.getNameResId())
        },
        selectedChipIndexes = selectedCompanionTypes.map { it.ordinal }.toIntArray(),
        onChipSelected = {
            onCompanionTypeChipSelected(OptionType.CompanionTypeOptionType.entries[it])
        }
    )

    Text(
        modifier = Modifier.padding(top = 32.dp),
        text = stringResource(R.string.available_walking_time),
        style = AconTheme.typography.head7_18_sb,
        color = AconTheme.color.White,
    )
    AconSlider(
        labels = AvailableWalkingTimeType.entries.map {
            stringResource(it.titleResId)
        },
        sliderIndex = selectedWalkingTime.ordinal,
        onSliderIndexChange = {
            onWalkingTimeChange(AvailableWalkingTimeType.entries[it])
        }
    )

    Text(
        modifier = Modifier.padding(top = 32.dp),
        text = stringResource(R.string.price_range),
        style = AconTheme.typography.head7_18_sb,
        color = AconTheme.color.White,
    )
    AconSlider(
        labels = RestaurantPriceRangeType.entries.map {
            stringResource(it.titleResId)
        },
        sliderIndex = selectedPriceRange.ordinal,
        onSliderIndexChange = {
            onPriceRangeChange(RestaurantPriceRangeType.entries[it])
        }
    )
}