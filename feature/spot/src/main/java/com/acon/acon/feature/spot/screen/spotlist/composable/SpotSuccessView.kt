package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.fog.fogBackground
import com.acon.acon.core.designsystem.effect.fog.getOverlayColor
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.spot.v2.Spot
import com.acon.acon.domain.type.TransportMode
import com.acon.acon.domain.type.UserType
import com.acon.acon.core.designsystem.R
import com.acon.acon.feature.spot.screen.spotlist.SpotListUiStateV2
import com.acon.core.ads_api.LocalSpotListAdProvider
import com.acon.feature.common.compose.LocalRequestSignIn
import com.acon.feature.common.compose.toDp
import dev.chrisbanes.haze.hazeSource
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.absoluteValue

private const val MAX_GUEST_AVAILABLE_COUNT = 5

@Composable
internal fun SpotListSuccessView(
    pagerState: PagerState,
    state: SpotListUiStateV2.Success,
    userType: UserType,
    onSpotClick: (Spot) -> Unit,
    onTryFindWay: (Spot) -> Unit,
    itemHeightPx: Float,
    modifier: Modifier = Modifier,
) {

    val adInsertedSpot: MutableList<Spot?> = state.spotList.toMutableList()
    when {
        adInsertedSpot.size >= 11 -> {
            adInsertedSpot.add(5, null)
            adInsertedSpot.add(10, null)
        }
        adInsertedSpot.size >= 6 -> {
            adInsertedSpot.add(5, null)
        }
        else -> {
            adInsertedSpot.add(null)
        }
    }

    val context = LocalContext.current
    val onSignInRequired = LocalRequestSignIn.current

    if (state.transportMode == TransportMode.BIKING) {
        SpotEmptyView(
            userType = userType,
            otherSpots = state.spotList.toImmutableList(),
            onSpotClick = onSpotClick,
            onTryFindWay = onTryFindWay,
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .hazeSource(LocalHazeState.current)
                .padding(horizontal = 16.dp)
        )
    } else {
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
            beyondViewportPageCount = 1,
            pageSize = PageSize.Fixed((itemHeightPx).toDp())
        ) { page ->
            val spot = adInsertedSpot[page]
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
                        text = stringResource(R.string.best_choice),
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
                if (spot != null) {
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
                                    onSignInRequired()
                                }
                                .fogBackground(
                                    glowColor = AconTheme.color.White,
                                )
                        )
                    } else {
                        SpotItem(
                            transportMode = state.transportMode,
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
                } else {
                    LocalSpotListAdProvider.current.NativeAd(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                shape = RoundedCornerShape(20.dp),
                                color = AconTheme.color.GlassBlackDefault
                            )
                    )
                }
            }

            LaunchedEffect(Unit) {
                if (spot != null) {
                    spotFogColor = if (spot.image.isBlank()) Color(0xFFE17651) else spot.image.getOverlayColor(context)
                }
            }
        }
    }
}