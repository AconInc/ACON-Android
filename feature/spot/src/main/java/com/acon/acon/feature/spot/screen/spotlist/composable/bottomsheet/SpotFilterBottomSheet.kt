package com.acon.acon.feature.spot.screen.spotlist.composable.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.type.SpotType
import com.acon.acon.feature.spot.R
import com.acon.acon.feature.spot.getNameResId
import com.acon.acon.feature.spot.state.ConditionState
import com.acon.acon.feature.spot.type.AvailableWalkingTimeType
import com.acon.acon.feature.spot.type.CafePriceRangeType
import com.acon.acon.feature.spot.type.RestaurantPriceRangeType
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.chrisbanes.haze.HazeState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotFilterBottomSheet(
    hazeState: HazeState,
    condition: ConditionState?,
    isFilteredResultFetching: Boolean,
    onComplete: (ConditionState) -> Unit,
    onReset: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {

    var selectedSpotType by rememberSaveable {
        mutableStateOf(
            condition?.spotType ?: SpotType.RESTAURANT
        )
    }
    var selectedRestaurantFeatures by rememberSaveable {
        mutableStateOf(
            condition?.restaurantFeatureOptionType ?: listOf()
        )
    }
    var selectedCafeFeatures by rememberSaveable {
        mutableStateOf(
            condition?.cafeFeatureOptionType ?: listOf()
        )
    }
    var selectedCompanionTypes by rememberSaveable {
        mutableStateOf(
            condition?.companionTypeOptionType ?: listOf()
        )
    }
    var selectedVisitPurposes by rememberSaveable {
        mutableStateOf(
            condition?.visitPurposeOptionType ?: listOf()
        )
    }
    var selectedRestaurantWalkingTime by rememberSaveable {
        mutableStateOf(
            condition?.restaurantWalkingTime ?: AvailableWalkingTimeType.UNDER_15_MINUTES
        )
    }
    var selectedCafeWalkingTime by rememberSaveable {
        mutableStateOf(
            condition?.cafeWalkingTime ?: AvailableWalkingTimeType.UNDER_15_MINUTES
        )
    }
    var selectedRestaurantPriceRange by rememberSaveable {
        mutableStateOf(
            condition?.restaurantPriceRange ?: RestaurantPriceRangeType.UNDER_10000
        )
    }
    var selectedCafePriceRange by rememberSaveable {
        mutableStateOf(
            condition?.cafePriceRange ?: CafePriceRangeType.UNDER_5000
        )
    }

    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(com.acon.acon.core.designsystem.R.raw.lottie_progress_w)
    )

    fun resetCafeFilter() {
        selectedCafeFeatures = listOf()
        selectedVisitPurposes = listOf()
        selectedCafeWalkingTime = AvailableWalkingTimeType.UNDER_15_MINUTES
        selectedCafePriceRange = CafePriceRangeType.UNDER_5000
    }

    fun resetRestaurantFilter() {
        selectedRestaurantFeatures = listOf()
        selectedCompanionTypes = listOf()
        selectedRestaurantWalkingTime = AvailableWalkingTimeType.UNDER_15_MINUTES
        selectedRestaurantPriceRange = RestaurantPriceRangeType.UNDER_10000
    }

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        containerColor = AconTheme.color.Dim_b_60,
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        dragHandle = null
    ) {
        // 필터 선택 여부 확인
        val isAnyFilterSelected = remember(
            selectedRestaurantFeatures,
            selectedCafeFeatures,
            selectedCompanionTypes,
            selectedVisitPurposes,
            selectedRestaurantWalkingTime,
            selectedCafeWalkingTime,
            selectedRestaurantPriceRange,
            selectedCafePriceRange
        ) {
            selectedRestaurantFeatures.isNotEmpty() ||
                    selectedCafeFeatures.isNotEmpty() ||
                    selectedCompanionTypes.isNotEmpty() ||
                    selectedVisitPurposes.isNotEmpty() ||
                    selectedRestaurantWalkingTime != AvailableWalkingTimeType.UNDER_15_MINUTES ||
                    selectedCafeWalkingTime != AvailableWalkingTimeType.UNDER_15_MINUTES ||
                    selectedRestaurantPriceRange != RestaurantPriceRangeType.UNDER_10000 ||
                    selectedCafePriceRange != CafePriceRangeType.UNDER_5000
        }

        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AconTheme.color.Dim_b_60)
                    .defaultHazeEffect(hazeState, tintColor = AconTheme.color.Dim_b_60)
            ) {
                Spacer(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp)
                        .clip(CircleShape)
                        .size(width = 36.dp, height = 5.dp)
                        .background(AconTheme.color.Gray5)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.filter_detail),
                        style = AconTheme.typography.head6_20_sb,
                        color = AconTheme.color.White
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 10.dp)
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(43.dp))
                    Text(
                        text = stringResource(R.string.visit_spot),
                        style = AconTheme.typography.head7_18_sb,
                        color = AconTheme.color.White,
                    )
                    SpotTypeTabRow(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .padding(top = 12.dp)
                            .background(
                                color = AconTheme.color.Gray8,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(4.dp),
                        selectedSpotType = selectedSpotType,
                        onSpotTypeSelected = {
                            selectedSpotType = it
                            when (selectedSpotType) {
                                SpotType.RESTAURANT -> {
                                    resetCafeFilter()
                                }

                                SpotType.CAFE -> {
                                    resetRestaurantFilter()
                                }
                            }
                        }
                    )

                    when (selectedSpotType) {
                        SpotType.RESTAURANT -> {
                            RestaurantBottomSheetContent(
                                selectedRestaurantFeatures = selectedRestaurantFeatures,
                                selectedCompanionTypes = selectedCompanionTypes,
                                selectedWalkingTime = selectedRestaurantWalkingTime,
                                selectedPriceRange = selectedRestaurantPriceRange,
                                onRestaurantFeatureChipSelected = {
                                    selectedRestaurantFeatures =
                                        if (selectedRestaurantFeatures.contains(it)) {
                                            selectedRestaurantFeatures - it
                                        } else {
                                            selectedRestaurantFeatures + it
                                        }
                                },
                                onCompanionTypeChipSelected = {
                                    selectedCompanionTypes =
                                        if (selectedCompanionTypes.contains(it)) {
                                            selectedCompanionTypes - it
                                        } else {
                                            selectedCompanionTypes + it
                                        }
                                }, onWalkingTimeChange = {
                                    selectedRestaurantWalkingTime = it
                                }, onPriceRangeChange = {
                                    selectedRestaurantPriceRange = it
                                }
                            )
                        }

                        SpotType.CAFE -> {
                            CafeBottomSheetContent(
                                selectedCafeFeatures = selectedCafeFeatures,
                                selectedVisitPurposes = selectedVisitPurposes,
                                selectedWalkingTime = selectedCafeWalkingTime,
                                selectedPriceRange = selectedCafePriceRange,
                                onCafeFeatureChipSelected = {
                                    selectedCafeFeatures =
                                        if (selectedCafeFeatures.contains(it)) {
                                            selectedCafeFeatures - it
                                        } else {
                                            selectedCafeFeatures + it
                                        }
                                },
                                onVisitPurposeChipSelected = {
                                    selectedVisitPurposes =
                                        if (selectedVisitPurposes.contains(it)) {
                                            selectedVisitPurposes - it
                                        } else {
                                            selectedVisitPurposes + it
                                        }
                                },
                                onWalkingTimeChange = {
                                    selectedCafeWalkingTime = it
                                },
                                onPriceRangeChange = {
                                    selectedCafePriceRange = it
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(140.dp))
                }
            }
            BottomCompleteRow(
                onReset = onReset,
                onComplete = {
                    onComplete(
                        ConditionState(
                            spotType = selectedSpotType,
                            restaurantFeatureOptionType = selectedRestaurantFeatures,
                            companionTypeOptionType = selectedCompanionTypes,
                            cafeFeatureOptionType = selectedCafeFeatures,
                            visitPurposeOptionType = selectedVisitPurposes,
                            restaurantWalkingTime = selectedRestaurantWalkingTime,
                            cafeWalkingTime = selectedCafeWalkingTime,
                            restaurantPriceRange = selectedRestaurantPriceRange,
                            cafePriceRange = selectedCafePriceRange
                        )
                    )
                },
                isFilteredResultFetching = isFilteredResultFetching,
                isAnyFilterSelected = isAnyFilterSelected,
                composition = lottieComposition,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(AconTheme.color.Black)
                    .defaultHazeEffect(hazeState, tintColor = AconTheme.color.Dim_b_30, alpha = .6f)
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp, bottom = 32.dp)
            )
        }
    }
}

@Composable
private fun SpotTypeTabRow(
    selectedSpotType: SpotType,
    onSpotTypeSelected: (SpotType) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
    ) {
        SpotType.entries.fastForEach {
            SpotTypeTab(
                spotType = it,
                modifier = Modifier
                    .weight(1f)
                    .noRippleClickable {
                        onSpotTypeSelected(it)
                    },
                isSelected = selectedSpotType == it
            )
        }
    }
}

@Composable
private fun SpotTypeTab(
    spotType: SpotType,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) AconTheme.color.White else AconTheme.color.Gray8),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(spotType.getNameResId()),
            style = AconTheme.typography.subtitle1_16_med,
            color = if (isSelected) AconTheme.color.Gray9 else AconTheme.color.Gray5
        )
    }
}

@Composable
private fun BottomCompleteRow(
    onReset: () -> Unit,
    onComplete: () -> Unit,
    isFilteredResultFetching: Boolean,
    isAnyFilterSelected: Boolean,
    composition: LottieComposition?,
    modifier: Modifier = Modifier
) {
    val isEnabled = isAnyFilterSelected && !isFilteredResultFetching

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.noRippleClickable {
                onReset()
            }, verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.ic_reset),
                modifier = Modifier.size(13.dp),
                contentDescription = stringResource(com.acon.acon.core.designsystem.R.string.reset_content_description),
                tint = AconTheme.color.White
            )
            Text(
                text = stringResource(R.string.filter_reset),
                style = AconTheme.typography.subtitle1_16_med,
                color = AconTheme.color.White,
                modifier = Modifier.padding(start = 7.dp)
            )
        }
        Box(
            modifier = Modifier
                .padding(start = 32.dp)
                .weight(1f)
                .clip(RoundedCornerShape(6.dp))
                .background(
                    color = if (isEnabled) AconTheme.color.Gray5 else AconTheme.color.Gray7
                )
                .padding(vertical = 12.dp)
                .noRippleClickable(enabled = isEnabled) { onComplete() },
            contentAlignment = Alignment.Center
        ) {
            if (isFilteredResultFetching) {
                LottieAnimation(
                    modifier = Modifier.size(21.dp),
                    composition = composition,
                    iterations = Int.MAX_VALUE,
                )
            } else {
                Text(
                    text = stringResource(R.string.filter_see_result),
                    style = AconTheme.typography.subtitle1_16_med,
                    color = if (isEnabled) AconTheme.color.White else AconTheme.color.Gray5
                )
            }
        }
    }
}

@Preview
@Composable
private fun SpotFilterBottomSheetPreview() {
    SpotFilterBottomSheet(
        hazeState = HazeState(),
        onDismissRequest = {},
        condition = null,
        onComplete = {},
        onReset = {},
        isFilteredResultFetching = false
    )
}