package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.animation.skeleton
import com.acon.acon.core.designsystem.component.bottomsheet.AconBottomSheet
import com.acon.acon.core.designsystem.component.bottomsheet.LoginBottomSheet
import com.acon.acon.core.designsystem.component.button.v2.AconFilledTextButton
import com.acon.acon.core.designsystem.component.button.v2.AconOutlinedTextButton
import com.acon.acon.core.designsystem.component.chip.AconChip
import com.acon.acon.core.designsystem.component.loading.SkeletonItem
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.effect.fog.fogBackground
import com.acon.acon.core.designsystem.effect.fog.getOverlayColor
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.spot.v2.SpotV2
import com.acon.acon.domain.type.CafeFilterType
import com.acon.acon.domain.type.FilterType
import com.acon.acon.domain.type.RestaurantFilterType
import com.acon.acon.domain.type.SpotType
import com.acon.acon.domain.type.UserType
import com.acon.acon.feature.spot.getNameResId
import com.acon.acon.feature.spot.mock.spotListUiStateRestaurantMock
import com.acon.acon.feature.spot.screen.component.SpotTypeToggle
import com.acon.acon.feature.spot.screen.spotlist.FilterDetailKey
import com.acon.acon.feature.spot.screen.spotlist.SpotListUiStateV2
import com.acon.feature.common.compose.toDp
import com.acon.feature.common.remember.rememberSocialRepository
import dev.chrisbanes.haze.hazeSource
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

private const val MAX_GUEST_AVAILABLE_COUNT = 5

@Composable
internal fun SpotListScreenV2(
    state: SpotListUiStateV2,
    modifier: Modifier = Modifier,
    userType: UserType = UserType.GUEST,
    onSpotTypeChanged: (SpotType) -> Unit = {},
    onSpotClick: (SpotV2) -> Unit = {},
    onTryFindWay: (SpotV2) -> Unit = {},
    onFilterButtonClick: () -> Unit = {},
    onFilterModalDismissRequest: () -> Unit = {},
    onRestaurantFilterSaved: (Map<FilterDetailKey, Set<RestaurantFilterType>>) -> Unit = {},
    onCafeFilterSaved: (Map<FilterDetailKey, Set<CafeFilterType>>) -> Unit = {},
    onGuestItemClick: () -> Unit = {},
    onGuestModalDismissRequest: () -> Unit = {},
) {

    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val screenHeightPx = with(LocalDensity.current) {
        screenHeightDp.toPx()
    }

    val itemHeightPx by remember {
        mutableFloatStateOf(screenHeightPx * .64f)
    }

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .defaultHazeEffect(
                    hazeState = LocalHazeState.current,
                    tintColor = AconTheme.color.Gray900,
                    blurRadius = 20.dp,
                )
                .padding(bottom = 14.dp, top = 6.dp)
                .statusBarsPadding()
        ) {
            SpotTypeToggle(
                selectedType = (state as? SpotListUiStateV2.Success)?.selectedSpotType ?: SpotType.RESTAURANT,
                onSwitched = onSpotTypeChanged,
                modifier = Modifier.align(Alignment.Center)
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_filter),
                contentDescription = stringResource(R.string.filter_content_description),
                tint = AconTheme.color.Gray50,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .clickable {
                        onFilterButtonClick()
                    }
                    .then(
                        if (state is SpotListUiStateV2.Success) {
                            if (state.selectedRestaurantFilters.values.flatten().isNotEmpty() ||
                                state.selectedCafeFilters.values.flatten().isNotEmpty()
                            ) Modifier.border(
                                width = 1.dp,
                                color = AconTheme.color.GlassWhiteSelected,
                                shape = CircleShape
                            ).background(
                                color = AconTheme.color.GlassWhiteSelected,
                                shape = CircleShape
                            ) else Modifier
                        } else Modifier
                    ).padding(6.dp)
            )
        }

        when(state) {
            is SpotListUiStateV2.Success -> {
                val socialRepository = rememberSocialRepository()

                val scope = rememberCoroutineScope()
                if(state.showLoginModal) {
                    LoginBottomSheet(
                        hazeState = LocalHazeState.current,
                        onDismissRequest = onGuestModalDismissRequest,
                        onGoogleSignIn = {
                            scope.launch {
                                socialRepository.googleLogin().onSuccess {
                                    onGuestModalDismissRequest()
                                }
                            }
                        }
                    )
                }
                when (state.selectedSpotType) {
                    SpotType.RESTAURANT -> {
                        if (state.showFilterModal) {
                            RestaurantFilterBottomSheet(
                                selectedItems = state.selectedRestaurantFilters.values.flatten().toImmutableSet(),
                                onComplete = onRestaurantFilterSaved,
                                onDismissRequest = onFilterModalDismissRequest
                            )
                        }
                        SpotListSuccessView(
                            state = state,
                            userType = userType,
                            onSpotClick = onSpotClick,
                            onTryFindWay = onTryFindWay,
                            itemHeightPx = itemHeightPx,
                            modifier = Modifier.fillMaxSize(),
                            onGuestItemClick = onGuestItemClick
                        )
                    }
                    SpotType.CAFE -> {
                        if (state.showFilterModal) {
                            CafeFilterBottomSheet(
                                selectedItems = state.selectedCafeFilters.values.flatten().toImmutableSet(),
                                onComplete = onCafeFilterSaved,
                                onDismissRequest = onFilterModalDismissRequest
                            )
                        }
                        SpotListSuccessView(
                            state = state,
                            userType = userType,
                            onSpotClick = onSpotClick,
                            onTryFindWay = onTryFindWay,
                            itemHeightPx = itemHeightPx,
                            modifier = Modifier.fillMaxSize(),
                            onGuestItemClick = onGuestItemClick
                        )
                    }
                }
            }

            is SpotListUiStateV2.Loading -> SpotListLoadingView(
                itemHeightPx = itemHeightPx,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .hazeSource(LocalHazeState.current)
                    .fillMaxSize()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 30.dp
                    )
            )
            is SpotListUiStateV2.LoadFailed -> {
                // TODO("Error")
            }
        }
    }
}

@Composable
private fun SpotListSuccessView(
    state: SpotListUiStateV2.Success,
    userType: UserType,
    onSpotClick: (SpotV2) -> Unit,
    onGuestItemClick: () -> Unit,
    onTryFindWay: (SpotV2) -> Unit,
    itemHeightPx: Float,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current

    val pagerState = rememberPagerState { state.spotList.size }

    VerticalPager(
        state = pagerState,
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = (itemHeightPx * .18f).toDp()
        ),
        modifier = modifier,
        flingBehavior = PagerDefaults.flingBehavior(
            state = pagerState,
            pagerSnapDistance = PagerSnapDistance.atMost(4),
            decayAnimationSpec = exponentialDecay(
                frictionMultiplier = 1.2f
            )
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        snapPosition = SnapPosition.Center,
        pageSize = PageSize.Fixed((itemHeightPx).toDp())
    ) { page ->
        val spot = state.spotList[page]
        var spotFogColor by remember {
            mutableStateOf(Color.Transparent)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .hazeSource(LocalHazeState.current)
                .padding(vertical = 6.dp)
                .fillMaxWidth(.91f)
                .graphicsLayer {
                    val pageOffset =
                        pagerState.getOffsetDistanceInPages(page).absoluteValue
                    val ratio = lerp(
                        start = 1.1f,
                        stop = 0.9f,
                        fraction = pageOffset.coerceIn(0f, 1f)
                    )
                    scaleX = ratio
                    scaleY = ratio
                }
        ) {
            if (page == 0) {
                Text(
                    text = "최고의 선택.",
                    style = AconTheme.typography.Title2,
                    fontWeight = FontWeight.SemiBold,
                    color = AconTheme.color.White,
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .fogBackground(
                            glowColor = AconTheme.color.White,
                            glowRadius = 100f
                        )
                        .zIndex(2f)
                )
            }
            if (page >= MAX_GUEST_AVAILABLE_COUNT && userType == UserType.GUEST) {
                SpotGuestItem(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            shape = RoundedCornerShape(20.dp),
                            color = AconTheme.color.GlassBlackDefault
                        )
                        .clickable {
                            onGuestItemClick()
                        }
                        .fogBackground(
                            glowColor = AconTheme.color.White,
                        )
                )
            } else {
                SpotItemV2(
                    spot = spot,
                    onItemClick = onSpotClick,
                    onFindWayButtonClick = onTryFindWay,
                    modifier = Modifier
                        .fillMaxSize()
                        .fogBackground(
                            glowColor = spotFogColor,
                            glowAlpha = 1f,
                        )
                        .zIndex(1f)
                )
            }
        }

        LaunchedEffect(Unit) {
            spotFogColor = spot.image.getOverlayColor(context)
        }
    }
}

@Composable
private fun SpotListLoadingView(
    itemHeightPx: Float,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SkeletonItem(
            modifier = Modifier
                .width(120.dp)
                .height(28.dp),
            shape = RoundedCornerShape(8.dp),
        )
        repeat(10) {
            Spacer(modifier = Modifier.height(14.dp))
            Column(
                modifier = Modifier
                    .height(itemHeightPx.toDp())
                    .fillMaxWidth()
                    .skeleton(
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(26.dp)
                ) {
                    SkeletonItem(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(3.5f),
                        shape = RoundedCornerShape(8.dp),
                    )
                    SkeletonItem(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxHeight()
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp),
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                SkeletonItem(
                    modifier = Modifier
                        .height(36.dp)
                        .width(140.dp)
                        .align(Alignment.End),
                    shape = RoundedCornerShape(8.dp),
                )
            }
        }
    }
}

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
private fun RestaurantFilterBottomSheet(
    selectedItems: ImmutableSet<RestaurantFilterType>,
    onComplete: (Map<FilterDetailKey, Set<RestaurantFilterType>>) -> Unit,
    onDismissRequest: () -> Unit,
) {
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
                    onClick = { /* TODO "Reset" */ },
                    modifier = Modifier.weight(3f)
                )
                AconFilledTextButton(
                    text = stringResource(R.string.see_result),
                    textStyle = AconTheme.typography.Body1.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    shape = CircleShape,
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
private fun CafeFilterBottomSheet(
    selectedItems: ImmutableSet<CafeFilterType>,
    onComplete: (Map<FilterDetailKey, Set<CafeFilterType>>) -> Unit,
    onDismissRequest: () -> Unit,
) {
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
                    onClick = { /* TODO "Reset" */ },
                    modifier = Modifier.weight(3f)
                )
                AconFilledTextButton(
                    text = stringResource(R.string.see_result),
                    textStyle = AconTheme.typography.Body1.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    shape = CircleShape,
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

@Composable
@Preview
private fun SpotListScreenV2Preview() {
    SpotListScreenV2(
        state = spotListUiStateRestaurantMock,
        modifier = Modifier
            .fillMaxWidth()
            .background(AconTheme.color.Gray900)
            .width(400.dp)
    )
}

@Composable
@Preview
private fun SpotListScreenV2LoadingPreview() {
    SpotListScreenV2(
        state = SpotListUiStateV2.Loading,
        modifier = Modifier
            .fillMaxWidth()
            .background(AconTheme.color.Gray900)
            .width(400.dp)
    )
}