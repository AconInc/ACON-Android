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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.bottomsheet.AconBottomSheet
import com.acon.acon.core.designsystem.component.button.v2.AconFilledTextButton
import com.acon.acon.core.designsystem.component.button.v2.AconOutlinedTextButton
import com.acon.acon.core.designsystem.component.chip.AconChip
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.type.CafeFilterType
import com.acon.acon.domain.type.FilterType
import com.acon.acon.domain.type.RestaurantFilterType
import com.acon.acon.feature.spot.getNameResId
import com.acon.acon.feature.spot.screen.spotlist.FilterDetailKey
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet

@OptIn(ExperimentalLayoutApi::class)
@Composable
private inline fun <reified T> EachFilterSpace(
    title: String,
    crossinline onFilterItemClick: (T) -> Unit,
    selectedItems: ImmutableSet<T>,
    modifier: Modifier = Modifier,
) where T : Enum<T>, T : FilterType {

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
                    title = stringResource((it as FilterType).getNameResId()),
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
    selectedItems: ImmutableSet<RestaurantFilterType>,
    onComplete: (Map<FilterDetailKey, Set<RestaurantFilterType>>) -> Unit,
    onReset: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    var completeButtonEnabled by remember {
        mutableStateOf(false)
    }
    val selectedRestaurantTypes = remember {
        mutableStateSetOf<RestaurantFilterType.RestaurantType>(
            *selectedItems.filterIsInstance<RestaurantFilterType.RestaurantType>().toTypedArray()
        )
    }
    val selectedRestaurantOperationTypes = remember {
        mutableStateSetOf<RestaurantFilterType.RestaurantOperationType>(
            *selectedItems.filterIsInstance<RestaurantFilterType.RestaurantOperationType>().toTypedArray()
        )
    }
    val selectedRestaurantPriceTypes = remember {
        mutableStateSetOf<RestaurantFilterType.RestaurantPriceType>(
            *selectedItems.filterIsInstance<RestaurantFilterType.RestaurantPriceType>().toTypedArray()
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

            EachFilterSpace<RestaurantFilterType.RestaurantType>(
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

            EachFilterSpace<RestaurantFilterType.RestaurantOperationType>(
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

            EachFilterSpace<RestaurantFilterType.RestaurantPriceType>(
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
                    modifier = Modifier.weight(3f)
                )
                AconFilledTextButton(
                    text = stringResource(R.string.see_result),
                    textStyle = AconTheme.typography.Body1.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    shape = CircleShape,
                    enabled = completeButtonEnabled && selectedRestaurantTypes.isNotEmpty(),
                    onClick = { onComplete(
                        mapOf(
                            RestaurantFilterType.RestaurantType::class to (selectedRestaurantTypes.toSet() as Set<RestaurantFilterType>),
                            RestaurantFilterType.RestaurantOperationType::class to (selectedRestaurantOperationTypes.toSet() as Set<RestaurantFilterType>),
                            RestaurantFilterType.RestaurantPriceType::class to (selectedRestaurantPriceTypes.toSet() as Set<RestaurantFilterType>)
                        )
                    ) },
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
    selectedItems: ImmutableSet<CafeFilterType>,
    onComplete: (Map<FilterDetailKey, Set<CafeFilterType>>) -> Unit,
    onReset: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    var completeButtonEnabled by remember {
        mutableStateOf(false)
    }
    val selectedCafeTypes = remember {
        mutableStateSetOf<CafeFilterType.CafeType>(
            *selectedItems.filterIsInstance<CafeFilterType.CafeType>().toTypedArray()
        )
    }
    val selectedCafeOperationTypes = remember {
        mutableStateSetOf<CafeFilterType.CafeOperationType>(
            *selectedItems.filterIsInstance<CafeFilterType.CafeOperationType>().toTypedArray()
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

            EachFilterSpace<CafeFilterType.CafeType>(
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

            EachFilterSpace<CafeFilterType.CafeOperationType>(
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
                    modifier = Modifier.weight(3f)
                )
                AconFilledTextButton(
                    text = stringResource(R.string.see_result),
                    textStyle = AconTheme.typography.Body1.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    shape = CircleShape,
                    enabled = completeButtonEnabled && selectedCafeTypes.isNotEmpty(),
                    onClick = {
                        onComplete(
                            mapOf(
                                CafeFilterType.CafeType::class to (selectedCafeTypes.toSet() as Set<CafeFilterType>),
                                CafeFilterType.CafeOperationType::class to (selectedCafeOperationTypes.toSet() as Set<CafeFilterType>)
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