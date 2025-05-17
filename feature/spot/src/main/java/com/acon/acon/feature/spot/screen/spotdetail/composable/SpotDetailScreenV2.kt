package com.acon.acon.feature.spot.screen.spotdetail.composable

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2.MenuBoardOverlay
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2.SignatureMenu
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2.StoreChip
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2.StoreFloatingButtonSet
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2.StoreImageIndicator
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2.bottomsheet.FindWayBottomSheet
import com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2.bottomsheet.ReportErrorBottomSheet
import com.acon.feature.common.compose.getTextSizeDp

@Composable
internal fun SpotDetailScreenV2(
    state: SpotDetailUiStateV2,
    onFindWayButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit = {},
) {
    val context = LocalContext.current
    val indicatorScrollState = rememberLazyListState()

    // TODO - 임시 리스트
    val imageList = listOf(
        R.drawable.ic_launcher_background,
        R.drawable.ic_error_1_120,
        R.drawable.ic_launcher_background,
        R.drawable.ic_error_1_120
    )

    // 임시 show 변수
    var showMenuDialog by remember { mutableStateOf(false) }
    var showReportErrorBottomSheet by remember { mutableStateOf(false) }
    var showFindWayBottomSheet by remember { mutableStateOf(false) }

    when (state) {
        SpotDetailUiStateV2.LoadFailed -> {}
        SpotDetailUiStateV2.Loading -> {}
        is SpotDetailUiStateV2.Success -> {

            val storeName = state.spotDetailInfo.name
            val storeImageList = state.spotDetailInfo.imageList
            val bottomPadding = if (storeImageList.size <= 1) { 34.dp } else { 0.dp }

            val pageCount = state.spotDetailInfo.imageList.size
            val pagerState = rememberPagerState(initialPage = 0, pageCount = { pageCount })

            Box(
                modifier = modifier
            ) {
                if (showReportErrorBottomSheet) {
                    ReportErrorBottomSheet(
                        hazeState = LocalHazeState.current,
                        onDismissRequest = { showReportErrorBottomSheet = false },
                        onClickReportError = {
                            val url = "https://walla.my/survey/ekYLYwpJv2d0Eznnijla" // TODO - 구글폼 (주소 나중에 따로 저장하여 불러와야 함)
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (showFindWayBottomSheet) {
                    FindWayBottomSheet(
                        hazeState = LocalHazeState.current,
                        onDismissRequest = { showFindWayBottomSheet = false },
                        onFindWay = { onFindWayButtonClick() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (showMenuDialog) {
                    MenuBoardOverlay(
                        imageList = imageList,
                        onDismiss = { showMenuDialog = false }
                    )
                }

                if (storeImageList.isNotEmpty()) {
                    HorizontalPager(
                        state = pagerState
                    ) { page ->
                        AsyncImage(
                            model = storeImageList[page],
                            contentDescription = "가게 이미지",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = AconTheme.color.GlassWhiteDefault
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.acon_line),
                                contentDescription = null,
                                modifier = Modifier.size(36.dp)
                            )

                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "장소 이미지를\n준비하고 있어요",
                                color = AconTheme.color.Gray200,
                                style = AconTheme.typography.Body1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer(Modifier.height(20.dp))
                    AconTopBar(
                        modifier = Modifier.padding(vertical = 16.dp),
                        paddingValues = PaddingValues(0.dp),
                        leadingIcon = {
                            IconButton(
                                onClick = { onNavigateToBack() }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left_28),
                                    contentDescription = "뒤로가기",
                                    tint = AconTheme.color.White
                                )
                            }
                        },
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (storeName.length > 9) storeName.take(9) + "…" else storeName,
                            style = AconTheme.typography.Title4,
                            color = AconTheme.color.White,
                        )

                        Spacer(Modifier.weight(1f))
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.acon_line),
                            contentDescription = stringResource(R.string.dotori_count_content_description),
                            tint = AconTheme.color.Gray50
                        )
                        Text(
                            text = "+8888", // TODO - spot.dotori
                            style = AconTheme.typography.Body1,
                            fontWeight = FontWeight.W400,
                            color = AconTheme.color.White,
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .width(
                                    getTextSizeDp("+9999", AconTheme.typography.Body1).width
                                ),
                            textAlign = TextAlign.End
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                    StoreChip(
                        modifier = Modifier.padding(start = 20.dp)
                    )

                    Spacer(Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = bottomPadding),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "대표메뉴",
                                color = AconTheme.color.White,
                                style = AconTheme.typography.Title4,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Spacer(Modifier.height(12.dp))
                            SignatureMenu(
                                signatureMenuList = state.spotDetailMenuList
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        StoreFloatingButtonSet(
                            onClickMenuBoard = {
                                showMenuDialog = true
                            },
                            onClickShare = {
                                val image = storeImageList.getOrElse(0) { "" }
                                val shareIntent = Intent.createChooser(
                                    Intent().apply {
                                        action = Intent.ACTION_SEND
                                        type = "image/*"
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "아콘에서 내 근처 ${state.spotDetailInfo.name} 확인해보세요!"
                                        )
                                        putExtra(Intent.EXTRA_STREAM, image)
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    },
                                    null
                                )
                                context.startActivity(shareIntent)
                            },
                            onClickMoreOptions = {
                                showReportErrorBottomSheet = true
                            },
                            isMenuBoarEnabled = state.spotDetailMenuList.isEmpty() //TODO - 메뉴 이미지가 없을 때
                        )
                    }

                    if (storeImageList.size >= 2) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            StoreImageIndicator(
                                pageCount = pageCount,
                                pagerState = pagerState,
                                indicatorScrollState = indicatorScrollState,
                                modifier = Modifier
                                    .width(66.dp)
                                    .padding(top = 16.dp, bottom = 12.dp)
                            )
                        }
                    }

                    AconFilledButton(
                        onClick = { showFindWayBottomSheet = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp)
                    ) {
                        Text(
                            text = "도보 0분 길찾기", // TODO - 예상 도착 시간 (도보)
                            color = AconTheme.color.White,
                            style = AconTheme.typography.Title4
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SpotDetailScreenV2Preview() {
    AconTheme {
        Box(
            modifier = Modifier.background(AconTheme.color.Black)
        ) {
            SpotDetailScreenV2(
                state = SpotDetailUiStateV2.Loading,
                onNavigateToBack = {},
                onFindWayButtonClick = {}
            )
        }
    }
}