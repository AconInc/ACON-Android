package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.bottombar.AconBottomBar
import com.acon.acon.core.designsystem.component.bottombar.BottomNavType
import com.acon.acon.core.designsystem.component.error.NetworkErrorView
import com.acon.acon.core.designsystem.component.popup.AconTextPopup
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.core.model.spot.Spot
import com.acon.core.type.CafeFilterType
import com.acon.core.type.RestaurantFilterType
import com.acon.core.type.SpotType
import com.acon.core.type.UserType
import com.acon.acon.feature.spot.mock.spotListUiStateRestaurantMock
import com.acon.acon.feature.spot.screen.component.SpotTypeToggle
import com.acon.acon.feature.spot.screen.spotlist.FilterDetailKey
import com.acon.acon.feature.spot.screen.spotlist.SpotListUiStateV2
import com.acon.core.ui.compose.LocalOnRetry
import com.acon.core.ui.compose.LocalRequestSignIn
import com.acon.core.ui.compose.LocalUserType
import com.acon.feature.common.compose.getScreenHeight
import com.acon.feature.common.intent.NavigationAppHandler
import dev.chrisbanes.haze.hazeSource
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.launch

@Composable
internal fun SpotListScreen(
    state: SpotListUiStateV2,
    modifier: Modifier = Modifier,
    onSpotTypeChanged: (SpotType) -> Unit = {},
    onSpotClick: (Spot, rank: Int) -> Unit = { _, _ -> },
    onTryFindWay: (Spot) -> Unit = {},
    onNavigationAppChoose: (NavigationAppHandler) -> Unit = {},
    onChooseNavigationAppModalDismiss: () -> Unit = {},
    onFilterButtonClick: () -> Unit = {},
    onFilterModalDismissRequest: () -> Unit = {},
    onRestaurantFilterSaved: (Map<FilterDetailKey, Set<RestaurantFilterType>>) -> Unit = {},
    onCafeFilterSaved: (Map<FilterDetailKey, Set<CafeFilterType>>) -> Unit = {},
    onNavigateToUploadScreen: () -> Unit = {},
    onNavigateToProfileScreen: () -> Unit = {},
) {
    val screenHeightDp = getScreenHeight()
    val screenHeightPx = with(LocalDensity.current) {
        screenHeightDp.toPx()
    }

    val itemHeightPx by remember {
        mutableFloatStateOf(screenHeightPx * .64f)
    }

    var pagerState = rememberPagerState { 0 }
    val scope = rememberCoroutineScope()

    val userType = LocalUserType.current
    val onSignInRequired = LocalRequestSignIn.current

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .defaultHazeEffect(
                    hazeState = LocalHazeState.current,
                    tintColor = AconTheme.color.GlassGray900,
                    blurRadius = 20.dp,
                )
                .padding(bottom = 14.dp, top = 6.dp)
                .statusBarsPadding()
        ) {
            SpotTypeToggle(
                selectedType = state.selectedSpotType,
                onSwitched = {
                    if (userType == UserType.GUEST)
                        onSignInRequired("click_toggle_guest?")
                    else
                        onSpotTypeChanged(it)
                },
                modifier = Modifier.align(Alignment.Center)
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_filter),
                contentDescription = stringResource(R.string.filter_content_description),
                tint = AconTheme.color.Gray50,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .noRippleClickable {
                        if (userType == UserType.GUEST)
                            onSignInRequired("")
                        else
                            onFilterButtonClick()
                    }
                    .then(
                        if (state is SpotListUiStateV2.Success) {
                            if (state.selectedRestaurantFilters.values.flatten().isNotEmpty() ||
                                state.selectedCafeFilters.values.flatten().isNotEmpty()
                            ) Modifier
                                .border(
                                    width = 1.dp,
                                    color = AconTheme.color.GlassWhiteSelected,
                                    shape = CircleShape
                                )
                                .background(
                                    color = AconTheme.color.GlassWhiteSelected,
                                    shape = CircleShape
                                ) else Modifier
                        } else Modifier
                    )
                    .padding(6.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (state) {
                is SpotListUiStateV2.Success -> {
                    Box(Modifier.fillMaxSize()) {
                        when (state.selectedSpotType) {
                            SpotType.RESTAURANT -> {
                                pagerState = rememberPagerState {
                                    val size = state.spotList.size
                                    when {
                                        size >= 11 -> size + 2
                                        size >= 5  -> size + 1
                                        else       -> size
                                    }
                                }
                                if (state.showFilterModal) {
                                    RestaurantFilterBottomSheet(
                                        selectedItems = state.selectedRestaurantFilters.values.flatten()
                                            .toImmutableSet(),
                                        onComplete = onRestaurantFilterSaved,
                                        onReset = LocalOnRetry.current,
                                        onDismissRequest = onFilterModalDismissRequest
                                    )
                                }
                                SpotListSuccessView(
                                    pagerState = pagerState,
                                    state = state,
                                    userType = userType,
                                    onSpotClick = onSpotClick,
                                    onTryFindWay = onTryFindWay,
                                    itemHeightPx = itemHeightPx,
                                    modifier = Modifier.fillMaxSize(),
                                    onNavigationAppChoose = onNavigationAppChoose,
                                    onChooseNavigationAppModalDismiss = onChooseNavigationAppModalDismiss
                                )
                            }

                            SpotType.CAFE -> {
                                pagerState = rememberPagerState {
                                    val size = state.spotList.size
                                    when {
                                        size >= 11 -> size + 2
                                        size >= 5  -> size + 1
                                        else       -> size
                                    }
                                }
                                if (state.showFilterModal) {
                                    CafeFilterBottomSheet(
                                        selectedItems = state.selectedCafeFilters.values.flatten()
                                            .toImmutableSet(),
                                        onComplete = onCafeFilterSaved,
                                        onReset = LocalOnRetry.current,
                                        onDismissRequest = onFilterModalDismissRequest
                                    )
                                }
                                SpotListSuccessView(
                                    pagerState = pagerState,
                                    state = state,
                                    userType = userType,
                                    onSpotClick = onSpotClick,
                                    onTryFindWay = onTryFindWay,
                                    itemHeightPx = itemHeightPx,
                                    modifier = Modifier.fillMaxSize(),
                                    onNavigationAppChoose = onNavigationAppChoose,
                                    onChooseNavigationAppModalDismiss = onChooseNavigationAppModalDismiss
                                )
                            }
                        }

                        if (state.showRefreshPopup)
                            AconTextPopup(
                                text = stringResource(R.string.refresh_spot),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                                    .padding(horizontal = 16.dp)
                                    .align(Alignment.BottomCenter),
                                onClick = LocalOnRetry.current
                            )
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
                            vertical = 48.dp
                        )
                )

                is SpotListUiStateV2.LoadFailed -> {
                    NetworkErrorView(
                        onRetry = LocalOnRetry.current,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is SpotListUiStateV2.OutOfServiceArea -> {
                    UnavailableLocationView(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 24.dp)
                    )
                }
            }
        }

        AconBottomBar(
            selectedItem = BottomNavType.SPOT,
            onItemClick = { bottomType ->
                when(bottomType) {
                    BottomNavType.SPOT -> {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }
                    BottomNavType.UPLOAD -> {
                        if (userType == UserType.GUEST) {
                            onSignInRequired("click_upload_guest?")
                        } else {
                            onNavigateToUploadScreen()
                        }
                    }
                    BottomNavType.PROFILE -> onNavigateToProfileScreen()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .defaultHazeEffect(
                    hazeState = LocalHazeState.current,
                    tintColor = AconTheme.color.GlassGray900
                )
                .navigationBarsPadding()
        )
    }
}

@Composable
@Preview
private fun SpotListScreenV2Preview() {
    SpotListScreen(
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
    SpotListScreen(
        state = SpotListUiStateV2.Loading(SpotType.RESTAURANT),
        modifier = Modifier
            .fillMaxWidth()
            .background(AconTheme.color.Gray900)
            .width(400.dp)
    )
}