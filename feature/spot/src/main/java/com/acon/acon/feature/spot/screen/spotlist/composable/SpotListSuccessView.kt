package com.acon.acon.feature.spot.screen.spotlist.composable

import android.location.Location
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.bottomsheet.AconBottomSheet
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.effect.shadowLayerBackground
import com.acon.acon.core.designsystem.effect.effect.getOverlayColor
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.core.model.spot.Spot
import com.acon.core.type.TransportMode
import com.acon.core.type.UserType
import com.acon.acon.feature.spot.screen.spotlist.SpotListUiStateV2
import com.acon.core.ads_api.LocalSpotListAdProvider
import com.acon.acon.core.ui.compose.LocalRequestSignIn
import com.acon.acon.core.ui.compose.toDp
import com.acon.acon.core.ui.android.KakaoNavigationAppHandler
import com.acon.acon.core.ui.android.NaverNavigationAppHandler
import com.acon.acon.core.ui.android.NavigationAppHandler
import dev.chrisbanes.haze.hazeSource
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.absoluteValue

private const val MAX_GUEST_AVAILABLE_COUNT = 5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SpotListSuccessView(
    pagerState: PagerState,
    state: SpotListUiStateV2.Success,
    userType: UserType,
    onSpotClick: (Spot, rank: Int) -> Unit,
    onTryFindWay: (Spot) -> Unit,
    itemHeightPx: Float,
    modifier: Modifier = Modifier,
    onNavigationAppChoose: (NavigationAppHandler) -> Unit = {},
    onChooseNavigationAppModalDismiss: () -> Unit = {},
) {

    val adInsertedSpot = remember(state.spotList) {
        val list: MutableList<Spot?> = state.spotList.toMutableList()

        if (list.size >= 11) {
            list.add(11, null)
        }
        if (list.size >= 5) {
            list.add(5, null)
        }

        list
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
            val spot = adInsertedSpot.getOrNull(page)

            if (state.showChooseNavigationAppModal && spot != null && pagerState.currentPage == page) {
                AconBottomSheet(
                    onDismissRequest = onChooseNavigationAppModalDismiss
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "네이버 지도",
                            modifier = Modifier
                                .fillMaxWidth()
                                .noRippleClickable {
                                    onNavigationAppChoose(
                                        NaverNavigationAppHandler(
                                            destination = Location("").apply {
                                                latitude = spot.latitude
                                                longitude = spot.longitude
                                            }, dName = spot.name,
                                            mode = state.transportMode
                                        )
                                    )
                                }
                                .padding(vertical = 17.dp, horizontal = 16.dp),
                            style = AconTheme.typography.Title4,
                            color = AconTheme.color.White,
                            fontWeight = FontWeight.W400
                        )
                        Text(
                            text = "카카오 지도",
                            modifier = Modifier
                                .fillMaxWidth()
                                .noRippleClickable {
                                    onNavigationAppChoose(
                                        KakaoNavigationAppHandler(
                                            start = state.currentLocation,
                                            destination = Location("").apply {
                                                latitude = spot.latitude
                                                longitude = spot.longitude
                                            },
                                            mode = state.transportMode
                                        )
                                    )
                                }
                                .padding(vertical = 17.dp, horizontal = 16.dp),
                            style = AconTheme.typography.Title4,
                            color = AconTheme.color.White,
                            fontWeight = FontWeight.W400
                        )
//                        Text(
//                            text = "구글 지도",
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .noRippleClickable {
//                                    onNavigationAppChoose(
//                                        GoogleNavigationAppHandler(
//                                            destination = Location("").apply {
//                                                latitude = spot.latitude
//                                                longitude = spot.longitude
//                                            },
//                                            mode = state.transportMode
//                                        )
//                                    )
//                                }
//                                .padding(vertical = 17.dp, horizontal = 16.dp),
//                            style = AconTheme.typography.Title4,
//                            color = AconTheme.color.White,
//                            fontWeight = FontWeight.W400
//                        )
                        Spacer(Modifier.height(31.dp))
                    }
                }
            }

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
                        try {
                            val pageOffset =
                                pagerState.getOffsetDistanceInPages(page).absoluteValue
                            val ratio = lerp(
                                start = 1.1f,
                                stop = 0.9f,
                                fraction = pageOffset.coerceIn(0f, 1f)
                            )
                            scaleX = ratio
                            scaleY = ratio
                        } catch (_: Exception) {
                        }
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
                            .shadowLayerBackground(
                                shadowColor = AconTheme.color.White,
                                shadowRadius = 100f
                            )
                            .zIndex(2f)
                    )
                }
                if (spot != null) {
                    if (page >= MAX_GUEST_AVAILABLE_COUNT && userType == UserType.GUEST) {
                        SpotGuestItem(
                            spot = spot,
                            onItemClick = { onSignInRequired("click_locked_detail_guest?") },
                            modifier = Modifier
                                .fillMaxSize()
                                .shadowLayerBackground(
                                    shadowColor = spotFogColor,
                                    shadowAlpha = 1f
                                )
                                .zIndex(1f)
                        )
                    } else {
                        SpotItem(
                            transportMode = state.transportMode,
                            spot = spot,
                            onItemClick = { onSpotClick(spot, page + 1) },
                            onFindWayButtonClick = onTryFindWay,
                            modifier = Modifier
                                .fillMaxSize()
                                .shadowLayerBackground(
                                    shadowColor = spotFogColor,
                                    shadowAlpha = 1f,
                                )
                                .zIndex(1f),
                            rank = (page + 1).takeIf { it <= 5 } ?: 0
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