package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.bottomsheet.LoginBottomSheet
import com.acon.acon.core.designsystem.component.error.NetworkErrorView
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.spot.v2.Spot
import com.acon.acon.domain.type.CafeFilterType
import com.acon.acon.domain.type.RestaurantFilterType
import com.acon.acon.domain.type.SpotType
import com.acon.acon.domain.type.UserType
import com.acon.acon.feature.spot.mock.spotListUiStateRestaurantMock
import com.acon.acon.feature.spot.screen.component.SpotTypeToggle
import com.acon.acon.feature.spot.screen.spotlist.FilterDetailKey
import com.acon.acon.feature.spot.screen.spotlist.SpotListUiStateV2
import com.acon.feature.common.compose.LocalOnRetry
import com.acon.feature.common.remember.rememberSocialRepository
import dev.chrisbanes.haze.hazeSource
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.launch

@Composable
internal fun SpotListScreen(
    state: SpotListUiStateV2,
    modifier: Modifier = Modifier,
    userType: UserType = UserType.GUEST,
    onSpotTypeChanged: (SpotType) -> Unit = {},
    onSpotClick: (Spot) -> Unit = {},
    onTryFindWay: (Spot) -> Unit = {},
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
                    .noRippleClickable {
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
                var pagerState = rememberPagerState {
                    state.spotList.size
                }

                val scope = rememberCoroutineScope()
                if(state.showLoginModal) {
                    LoginBottomSheet(
                        hazeState = LocalHazeState.current,
                        onDismissRequest = onGuestModalDismissRequest,
                        onGoogleSignIn = {
                            scope.launch {
                                socialRepository.googleLogin().onSuccess {
                                    onGuestModalDismissRequest()
                                    pagerState.scrollToPage(0)
                                }
                            }
                        }
                    )
                }
                when (state.selectedSpotType) {
                    SpotType.RESTAURANT -> {
                        pagerState = rememberPagerState {
                            state.spotList.size
                        }
                        if (state.showFilterModal) {
                            RestaurantFilterBottomSheet(
                                selectedItems = state.selectedRestaurantFilters.values.flatten().toImmutableSet(),
                                onComplete = onRestaurantFilterSaved,
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
                            onGuestItemClick = onGuestItemClick
                        )
                    }
                    SpotType.CAFE -> {
                        pagerState = rememberPagerState {
                            state.spotList.size
                        }
                        if (state.showFilterModal) {
                            CafeFilterBottomSheet(
                                selectedItems = state.selectedCafeFilters.values.flatten().toImmutableSet(),
                                onComplete = onCafeFilterSaved,
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
                NetworkErrorView(
                    onRetry = LocalOnRetry.current,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
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
        state = SpotListUiStateV2.Loading,
        modifier = Modifier
            .fillMaxWidth()
            .background(AconTheme.color.Gray900)
            .width(400.dp)
    )
}