package com.acon.acon.feature.spot.screen.spotdetail.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.component.loading.SkeletonItem
import com.acon.acon.core.designsystem.dropShadow
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.spot.SpotDetailInfo
import com.acon.acon.domain.type.SpotType
import com.acon.acon.feature.spot.screen.spotdetail.SpotDetailUiState
import com.acon.acon.feature.spot.screen.spotdetail.amplitude.spotDetailAmplitudeDuration
import com.acon.acon.feature.spot.screen.spotdetail.amplitude.spotDetailAmplitudeFindWay
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.MenuItem
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.MoveToTopFAB
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.RestaurantBottomActionBar
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.SpotChip
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.SpotDetailTopBar
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.launch

@Composable
internal fun SpotDetailScreen(
    state: SpotDetailUiState,
    modifier: Modifier = Modifier,
    onNavigateToSpotListView: () -> Unit = {},
    onFindWayButtonClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val scrollIsAtTop by remember {
        derivedStateOf {
            scrollState.value == 0
        }
    }

    val startTime = rememberSaveable { System.currentTimeMillis() }
    DisposableEffect(Unit) {
        onDispose {
            spotDetailAmplitudeDuration(startTime)
        }
    }

    Surface(
        modifier = modifier,
        color = AconTheme.color.Gray9
    ) {
        when(state) {
            is SpotDetailUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        SpotDetailTopBar(
                            storeName = state.spotDetailInfo.name,
                            spotType = state.spotDetailInfo.spotType,
                            onLeadingIconClicked = onNavigateToSpotListView,
                            modifier = Modifier
                                .background(AconTheme.color.Black)
                                .defaultHazeEffect(
                                    hazeState = LocalHazeState.current,
                                    tintColor = AconTheme.color.Dim_b_30,
                                )
                                .zIndex(1f)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                                .padding(top = 52.dp)
                                .hazeSource(LocalHazeState.current)
                        ) {
                            Box {
                                AsyncImage(
                                    model = state.spotDetailInfo.imageList[0],
                                    contentDescription = stringResource(com.acon.acon.feature.spot.R.string.spot_store_image),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(360f / 290f)
                                        .hazeSource(LocalHazeState.current),
                                    contentScale = ContentScale.Crop,
                                )
                                AsyncImage(
                                    model = state.spotDetailInfo.imageList[0],
                                    contentDescription = stringResource(com.acon.acon.feature.spot.R.string.spot_store_image),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(360f / 290f)
                                        .offset(y = 58.dp)
                                        .hazeSource(LocalHazeState.current),
                                    contentScale = ContentScale.Crop,
                                )
                            }

                            SpotChip(
                                selected = state.spotDetailInfo.openStatus,
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 78.dp)
                                    .hazeSource(LocalHazeState.current)
                            )
                            Row(
                                modifier = Modifier
                                    .padding(start = 20.dp, top = 8.dp)
                                    .hazeSource(LocalHazeState.current)
                            ) {
                                Image(
                                    imageVector = ImageVector.vectorResource(
                                        com.acon.acon.core.designsystem.R.drawable.ic_location_gray_20
                                    ),
                                    contentDescription = stringResource(com.acon.acon.feature.spot.R.string.spot_gps_icon)
                                )
                                Text(
                                    text = state.spotDetailInfo.address,
                                    style = AconTheme.typography.body2_14_reg,
                                    color = AconTheme.color.Gray4,
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(AconTheme.color.Gray9)
                                    .padding(top = 40.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(start = 20.dp)
                                        .width(intrinsicSize = IntrinsicSize.Max),
                                ) {
                                    Text(
                                        text = stringResource(com.acon.acon.feature.spot.R.string.spot_detail_tab_menu),
                                        style = AconTheme.typography.subtitle1_16_med,
                                        color = AconTheme.color.White,
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp, vertical = 10.dp)
                                    )
                                    HorizontalDivider(
                                        color = AconTheme.color.White,
                                        thickness = 2.dp,
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                    )
                                }
                            }

                            List(state.spotDetailMenuList.size) { index ->
                                MenuItem(
                                    menu = state.spotDetailMenuList[index],
                                    modifier = Modifier
                                        .background(AconTheme.color.Gray9)
                                        .padding(start = 20.dp, end = 20.dp, top = 17.dp)
                                        .hazeSource(LocalHazeState.current),
                                )
                            }
                        }

                        MoveToTopFAB(
                            modifier = Modifier
                                .align(alignment = Alignment.BottomEnd)
                                .padding(end = 20.dp, bottom = 16.dp)
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    color = AconTheme.color.Gray7,
                                    shape = CircleShape
                                )
                                .border(
                                    width = 1.dp,
                                    color = AconTheme.color.Gray6,
                                    shape = CircleShape
                                )
                                .dropShadow(
                                    shape = CircleShape,
                                    color = AconTheme.color.Fab_shaodw_1,
                                    blur = 4.dp,
                                    offsetX = (0).dp,
                                    offsetY = 2.dp,
                                )
                                .defaultHazeEffect(
                                    hazeState = LocalHazeState.current,
                                    tintColor = AconTheme.color.Dim_b_30,
                                    blurRadius = 24.dp
                                ),
                            onClickFab = {
                                scope.launch {
                                    if (!scrollIsAtTop) {
                                        scrollState.animateScrollTo(0)
                                    }
                                }
                            }
                        )
                    }

                    RestaurantBottomActionBar(
                        localAcornCount = state.spotDetailInfo.localAcornCount,
                        basicAcornCount = state.spotDetailInfo.basicAcornCount,
                        onClickFindDirections = {
                            onFindWayButtonClick()
                            spotDetailAmplitudeFindWay()
                        },
                        modifier = Modifier
                            .background(AconTheme.color.Gray9)
                            .defaultHazeEffect(
                                hazeState = LocalHazeState.current,
                                tintColor = AconTheme.color.Dim_b_30,
                            )
                    )
                }
            }

            is SpotDetailUiState.Loading -> {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxSize()
                        .padding()
                ) {
                    Spacer(modifier = Modifier.height(58.dp))
                    SkeletonItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .aspectRatio(254f / 30f)
                            .padding(start = 16.dp, end = 90.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SkeletonItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(360f / 296f)
                    )
                    SkeletonItem(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .size(width = 64.dp, height = 28.dp)
                    )
                    SkeletonItem(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .size(width = 180.dp, height = 20.dp)
                    )
                    SkeletonItem(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 40.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .size(width = 64.dp, height = 38.dp)
                    )
                    repeat(8) {
                        Row (
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 30.dp)
                                    .weight(1f)
                            ) {
                                SkeletonItem(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .size(width = 58.dp, height = 22.dp)
                                )
                                SkeletonItem(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .size(width = 121.dp, height = 24.dp)
                                )
                            }
                            SkeletonItem(
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .size(78.dp)
                            )
                        }
                    }
                }
            }

            is SpotDetailUiState.LoadFailed -> {
                // TODO : 로드 실패 뷰
            }


        }
    }
}

@Preview
@Composable
private fun SpotDetailScreenPreview() {
    AconTheme {
        SpotDetailScreen(
            state = SpotDetailUiState.Success(
                SpotDetailInfo(
                    id = 1,
                    name = "",
                    spotType = SpotType.CAFE,
                    imageList = emptyList(),
                    openStatus = true,
                    address = "경기도 고양시 고양구 고양이",
                    localAcornCount = 1,
                    basicAcornCount = 1,
                    latitude = 1.11,
                    longitude = 1.11
                ),
                spotDetailMenuList = emptyList()
            ),
            onNavigateToSpotListView = {},
            onFindWayButtonClick = {}
        )
    }
}

@Preview
@Composable
private fun SpotDetailLoadingScreenPreview() {
    AconTheme {
        SpotDetailScreen(
            state = SpotDetailUiState.Loading,
            onNavigateToSpotListView = {},
            onFindWayButtonClick = {}
        )
    }
}
