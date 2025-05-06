package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.glassmorphism.LocalHazeState
import com.acon.acon.core.designsystem.glassmorphism.defaultHazeEffect
import com.acon.acon.core.designsystem.glassmorphism.glowBackground
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.spot.v2.SpotV2
import com.acon.acon.domain.type.SpotType
import com.acon.acon.feature.spot.mock.spotListUiStateMock
import com.acon.acon.feature.spot.screen.component.SpotTypeToggle
import com.acon.acon.feature.spot.screen.spotlist.SpotListUiStateV2
import com.acon.feature.common.compose.toDp
import dev.chrisbanes.haze.hazeSource
import kotlin.math.absoluteValue

@Composable
internal fun SpotListScreenV2(
    state: SpotListUiStateV2,
    onSpotTypeChanged: (SpotType) -> Unit,
    onSpotClick: (SpotV2) -> Unit,
    onTryFindWay: (spotId: Long) -> Unit,
    modifier: Modifier = Modifier,
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
                    blurRadius = 20.dp
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
                        // TODO("Filter Clicked")
                    }
            )
        }

        when(state) {
            is SpotListUiStateV2.Success -> {
                val pagerState = rememberPagerState { state.spotList.size }
                VerticalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 50.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .hazeSource(LocalHazeState.current),
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
                                    fraction = pageOffset
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
                                    .glowBackground(glowRadius = 100f)
                            )
                        }
                        SpotItemV2(
                            spot = state.spotList[page],
                            onItemClick = onSpotClick,
                            onFindWayButtonClick = onTryFindWay,
                            modifier = Modifier
                                .fillMaxSize()
                                .glowBackground()
                        )
                    }
                }
            }

            is SpotListUiStateV2.Loading -> {
                // TODO("Loading")
            }
            is SpotListUiStateV2.LoadFailed -> {
                // TODO("Error")
            }
        }
    }
}

@Composable
@Preview
private fun SpotListScreenV2Preview() {
    SpotListScreenV2(
        state = spotListUiStateMock,
        onSpotTypeChanged = {},
        onSpotClick = {},
        onTryFindWay = {},
        modifier = Modifier
            .fillMaxWidth()
            .background(AconTheme.color.Gray900)
            .width(400.dp)
    )
}