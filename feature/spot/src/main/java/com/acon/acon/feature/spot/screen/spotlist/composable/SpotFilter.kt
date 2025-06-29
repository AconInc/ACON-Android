package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.acon.acon.core.common.utils.toHHmmss
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.bottomsheet.AconBottomSheet
import com.acon.acon.core.designsystem.component.button.v2.AconFilledTextButton
import com.acon.acon.core.designsystem.component.button.v2.AconOutlinedTextButton
import com.acon.acon.core.designsystem.component.chip.AconChip
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.spot.getNameResId
import com.acon.acon.feature.spot.screen.spotlist.FilterDetailKey
import com.acon.acon.core.analytics.amplitude.AconAmplitude
import com.acon.acon.core.analytics.constants.EventNames
import com.acon.acon.core.analytics.constants.PropertyKeys
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import java.time.LocalTime

@OptIn(ExperimentalLayoutApi::class)
@Composable
private inline fun <reified T> EachFilterSpace(
    title: String,
    crossinline onFilterItemClick: (T) -> Unit,
    selectedItems: ImmutableSet<T>,
    modifier: Modifier = Modifier,
) where T : Enum<T>, T : com.acon.acon.core.model.type.FilterType {

    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = AconTheme.typography.Title5,
            fontWeight = FontWeight.SemiBold,
            color = AconTheme.color.White,
        )
        FlowRow(
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            enumValues<T>().forEach {
                AconChip(
                    title = stringResource((it as com.acon.acon.core.model.type.FilterType).getNameResId()),
                    isSelected = selectedItems.contains(it),
                    onClick = { onFilterItemClick(it) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RestaurantFilterBottomSheet(
    selectedItems: ImmutableSet<com.acon.acon.core.model.type.RestaurantFilterType>,
    onComplete: (Map<FilterDetailKey, Set<com.acon.acon.core.model.type.RestaurantFilterType>>) -> Unit,
    onReset: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    var completeButtonEnabled by remember {
        mutableStateOf(false)
    }
    val selectedRestaurantTypes = remember {
        mutableStateSetOf<com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType>(
            *selectedItems.filterIsInstance<com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType>().toTypedArray()
        )
    }
    val selectedRestaurantOperationTypes = remember {
        mutableStateSetOf<com.acon.acon.core.model.type.RestaurantFilterType.RestaurantOperationType>(
            *selectedItems.filterIsInstance<com.acon.acon.core.model.type.RestaurantFilterType.RestaurantOperationType>().toTypedArray()
        )
    }
    val selectedRestaurantPriceTypes = remember {
        mutableStateSetOf<com.acon.acon.core.model.type.RestaurantFilterType.RestaurantPriceType>(
            *selectedItems.filterIsInstance<com.acon.acon.core.model.type.RestaurantFilterType.RestaurantPriceType>().toTypedArray()
        )
    }

    AconBottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 26.dp)
        ) {
            Text(
                text = stringResource(R.string.filter_detail),
                style = AconTheme.typography.Title3,
                fontWeight = FontWeight.SemiBold,
                color = AconTheme.color.White,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally)
            )

            EachFilterSpace<com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType>(
                title = stringResource(R.string.type),
                onFilterItemClick = {
                    if (selectedRestaurantTypes.contains(it)) {
                        selectedRestaurantTypes.remove(it)
                    } else {
                        completeButtonEnabled = true
                        selectedRestaurantTypes.add(it)
                    }
                },
                modifier = Modifier,
                selectedItems = selectedRestaurantTypes.toImmutableSet()
            )
            Spacer(modifier = Modifier.height(40.dp))

            EachFilterSpace<com.acon.acon.core.model.type.RestaurantFilterType.RestaurantOperationType>(
                title = stringResource(R.string.operation_time),
                onFilterItemClick = {
                    if (selectedRestaurantOperationTypes.contains(it)) {
                        selectedRestaurantOperationTypes.remove(it)
                    } else {
                        completeButtonEnabled = true
                        selectedRestaurantOperationTypes.add(it)
                    }
                },
                modifier = Modifier,
                selectedItems = selectedRestaurantOperationTypes.toImmutableSet()
            )
            Spacer(modifier = Modifier.height(40.dp))

            EachFilterSpace<com.acon.acon.core.model.type.RestaurantFilterType.RestaurantPriceType>(
                title = stringResource(R.string.price),
                onFilterItemClick = {
                    if (selectedRestaurantPriceTypes.contains(it)) {
                        selectedRestaurantPriceTypes.remove(it)
                    } else {
                        completeButtonEnabled = true
                        selectedRestaurantPriceTypes.add(it)
                    }
                },
                modifier = Modifier,
                selectedItems = selectedRestaurantPriceTypes.toImmutableSet()
            )
            Spacer(modifier = Modifier.height(99.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AconOutlinedTextButton(
                    text = stringResource(R.string.reset),
                    textStyle = AconTheme.typography.Body1.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    onClick = onReset,
                    contentPadding = PaddingValues(vertical = 12.dp),
                    modifier = Modifier.weight(3f)
                )

                val context = LocalContext.current
                AconFilledTextButton(
                    text = stringResource(R.string.see_result),
                    textStyle = AconTheme.typography.Body1.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    shape = CircleShape,
                    enabled = completeButtonEnabled &&
                            (selectedRestaurantTypes.isNotEmpty() || selectedRestaurantOperationTypes.isNotEmpty() || selectedRestaurantPriceTypes.isNotEmpty()),
                    onClick = {
                        if (selectedRestaurantTypes.isNotEmpty()) {
                            AconAmplitude.trackEvent(
                                eventName = EventNames.FILTER_RESTAURANT,
                                property = PropertyKeys.CLICK_FILTER_TYPE_RESTAURANT to selectedRestaurantTypes.map {
                                    context.getString(it.getNameResId())
                                }
                            )
                        }
                        if (selectedRestaurantOperationTypes.isNotEmpty()) {
                            AconAmplitude.trackEvent(
                                eventName = EventNames.FILTER_RESTAURANT,
                                property = PropertyKeys.CLICK_FILTER_TIME_RESTAURANT to true
                            )
                            AconAmplitude.trackEvent(
                                eventName = EventNames.FILTER_RESTAURANT,
                                property = PropertyKeys.RECORD_FILTER_TIME_RESTAURANT to LocalTime.now().toHHmmss()
                            )
                        }
                        if (selectedRestaurantPriceTypes.isNotEmpty()) {
                            AconAmplitude.trackEvent(
                                eventName = EventNames.FILTER_RESTAURANT,
                                property = PropertyKeys.CLICK_FILTER_PRICE_RESTAURANT to true
                            )
                        }
                        onComplete(
                            mapOf(
                                com.acon.acon.core.model.type.RestaurantFilterType.RestaurantType::class to (selectedRestaurantTypes.toSet() as Set<com.acon.acon.core.model.type.RestaurantFilterType>),
                                com.acon.acon.core.model.type.RestaurantFilterType.RestaurantOperationType::class to (selectedRestaurantOperationTypes.toSet() as Set<com.acon.acon.core.model.type.RestaurantFilterType>),
                                com.acon.acon.core.model.type.RestaurantFilterType.RestaurantPriceType::class to (selectedRestaurantPriceTypes.toSet() as Set<com.acon.acon.core.model.type.RestaurantFilterType>)
                            )
                        )
                    },
                    contentPadding = PaddingValues(
                        vertical = 12.dp,
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(5f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CafeFilterBottomSheet(
    selectedItems: ImmutableSet<com.acon.acon.core.model.type.CafeFilterType>,
    onComplete: (Map<FilterDetailKey, Set<com.acon.acon.core.model.type.CafeFilterType>>) -> Unit,
    onReset: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    var completeButtonEnabled by remember {
        mutableStateOf(false)
    }
    val selectedCafeTypes = remember {
        mutableStateSetOf<com.acon.acon.core.model.type.CafeFilterType.CafeType>(
            *selectedItems.filterIsInstance<com.acon.acon.core.model.type.CafeFilterType.CafeType>().toTypedArray()
        )
    }
    val selectedCafeOperationTypes = remember {
        mutableStateSetOf<com.acon.acon.core.model.type.CafeFilterType.CafeOperationType>(
            *selectedItems.filterIsInstance<com.acon.acon.core.model.type.CafeFilterType.CafeOperationType>().toTypedArray()
        )
    }

    AconBottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 26.dp)
        ) {
            Text(
                text = stringResource(R.string.filter_detail),
                style = AconTheme.typography.Title3,
                fontWeight = FontWeight.SemiBold,
                color = AconTheme.color.White,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally)
            )

            EachFilterSpace<com.acon.acon.core.model.type.CafeFilterType.CafeType>(
                title = stringResource(R.string.type),
                onFilterItemClick = {
                    if (selectedCafeTypes.contains(it)) {
                        selectedCafeTypes.remove(it)
                    } else {
                        completeButtonEnabled = true
                        selectedCafeTypes.add(it)
                    }
                },
                modifier = Modifier,
                selectedItems = selectedCafeTypes.toImmutableSet()
            )
            Spacer(modifier = Modifier.height(40.dp))

            EachFilterSpace<com.acon.acon.core.model.type.CafeFilterType.CafeOperationType>(
                title = stringResource(R.string.operation_time),
                onFilterItemClick = {
                    if (selectedCafeOperationTypes.contains(it)) {
                        selectedCafeOperationTypes.remove(it)
                    } else {
                        completeButtonEnabled = true
                        selectedCafeOperationTypes.add(it)
                    }
                },
                modifier = Modifier,
                selectedItems = selectedCafeOperationTypes.toImmutableSet()
            )
            Spacer(modifier = Modifier.height(99.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AconOutlinedTextButton(
                    text = stringResource(R.string.reset),
                    textStyle = AconTheme.typography.Body1.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    onClick = onReset,
                    contentPadding = PaddingValues(vertical = 12.dp),
                    modifier = Modifier.weight(3f)
                )

                val context = LocalContext.current
                AconFilledTextButton(
                    text = stringResource(R.string.see_result),
                    textStyle = AconTheme.typography.Body1.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    shape = CircleShape,
                    enabled = completeButtonEnabled && (selectedCafeTypes.isNotEmpty() || selectedCafeOperationTypes.isNotEmpty()),
                    onClick = {
                        if (selectedCafeTypes.isNotEmpty()) {
                            AconAmplitude.trackEvent(
                                eventName = EventNames.FILTER_CAFE,
                                property = PropertyKeys.CLICK_FILTER_TYPE_CAFE to selectedCafeTypes.map {
                                    context.getString(it.getNameResId())
                                }
                            )
                        }
                        if (selectedCafeOperationTypes.isNotEmpty()) {
                            AconAmplitude.trackEvent(
                                eventName = EventNames.FILTER_CAFE,
                                property = PropertyKeys.CLICK_FILTER_TIME_CAFE to true
                            )
                        }
                        onComplete(
                            mapOf(
                                com.acon.acon.core.model.type.CafeFilterType.CafeType::class to (selectedCafeTypes.toSet() as Set<com.acon.acon.core.model.type.CafeFilterType>),
                                com.acon.acon.core.model.type.CafeFilterType.CafeOperationType::class to (selectedCafeOperationTypes.toSet() as Set<com.acon.acon.core.model.type.CafeFilterType>)
                            )
                        )
                    },
                    contentPadding = PaddingValues(
                        vertical = 12.dp,
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(5f)
                )
            }
        }
    }
}