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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.acon.acon.core.common.UrlConstants
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.imageGradientLayer
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.feature.common.compose.getTextSizeDp
import dev.chrisbanes.haze.hazeSource
import okhttp3.internal.immutableListOf

@Composable
internal fun SpotDetailScreen(
    state: SpotDetailUiState,
    onFindWayButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    onClickAddBookmark: () -> Unit = {},
    onClickDeleteBookmark: () -> Unit = {},
    onClickMenuBoard: () -> Unit = {},
    onDismissMenuBoard: () -> Unit = {},
    onRequestErrorReportModal: () -> Unit = {},
    onDismissErrorReportModal: () -> Unit = {},
    onRequestFindWayModal: () -> Unit = {},
    onDismissFindWayModal: () -> Unit = {},
    onNavigateToBack: () -> Unit = {},
) {
    val context = LocalContext.current
    val indicatorScrollState = rememberLazyListState()

    // TODO - 임시 메뉴판 이미지 리스트
    val imageList = listOf(
        R.drawable.ic_launcher_background,
        R.drawable.ic_error_1_120,
        R.drawable.ic_launcher_background,
        R.drawable.ic_error_1_120
    )

    val noStoreText = immutableListOf(
        stringResource(R.string.no_store_image_verified),
        stringResource(R.string.no_store_image_secret),
        stringResource(R.string.no_store_image_mystery)
    )

    when (state) {
        SpotDetailUiState.LoadFailed -> {}
        SpotDetailUiState.Loading -> {}
        is SpotDetailUiState.Success -> {
            val storeName = state.spotDetailInfo.name
            val storeImageList = state.spotDetailInfo.imageList
            val acornCount = 99 //TODO - state.spotDetailInfo.acornCount
            val bottomPadding = if (storeImageList.size <= 1) {
                34.dp
            } else {
                0.dp
            }

            val pageCount = state.spotDetailInfo.imageList.size
            val pagerState = rememberPagerState(initialPage = 0, pageCount = { pageCount })

            Box(
                modifier = modifier.navigationBarsPadding()
            ) {
                if (state.showReportErrorModal) {
                    ReportErrorBottomSheet(
                        onDismissRequest = { onDismissErrorReportModal() },
                        onClickReportError = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(UrlConstants.ERROR_REPORT))
                            context.startActivity(intent)
                        }
                    )
                }

                if (state.showFindWayModal) {
                    FindWayBottomSheet(
                        onFindWay = { onFindWayButtonClick() },
                        onDismissRequest = { onDismissFindWayModal() }
                    )
                }

                if (state.showMenuBoardDialog) {
                    MenuBoardOverlay(
                        imageList = imageList,
                        onDismiss = { onDismissMenuBoard() }
                    )
                }

                if (storeImageList.isNotEmpty()) {
                    HorizontalPager(
                        state = pagerState
                    ) { page ->
                        AsyncImage(
                            model = storeImageList[page],
                            contentDescription = stringResource(R.string.store_background_image_content_description),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .imageGradientLayer(
                                    startColor = AconTheme.color.Gray900.copy(alpha = 0.8f)
                                )
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_background_no_store),
                            contentDescription = stringResource(R.string.no_store_background_image_content_description),
                            modifier = Modifier
                                .fillMaxSize()
                                .imageGradientLayer(
                                    startColor = AconTheme.color.Gray900.copy(alpha = 0.8f),
                                ),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.acon_line),
                                contentDescription = stringResource(R.string.acorn_count_content_description),
                                modifier = Modifier.size(36.dp)
                            )

                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = noStoreText.random(),
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
                        .hazeSource(LocalHazeState.current),
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
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_topbar_arrow_left),
                                    contentDescription = stringResource(R.string.back),
                                    tint = AconTheme.color.Gray50
                                )
                            }
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_ellipsis),
                                contentDescription = stringResource(R.string.floating_btn_more_option),
                                tint = AconTheme.color.Gray50,
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .noRippleClickable { onRequestErrorReportModal() }
                            )
                        }
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
                            contentDescription = stringResource(R.string.acorn_count_content_description),
                            tint = AconTheme.color.Gray50
                        )

                        Text(
                            text = if (acornCount > 9999) stringResource(R.string.over_max_acon_count) else acornCount.toString(),
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
                    StoreTagRow(
                        isNew = true, // TODO - state.spotDetailInfo.tagList.contains(stringResource(R.string.store_tag_new)),
                        isLocal = true, // TODO - state.spotDetailInfo.tagList.contains(stringResource(R.string.store_tag_local)),
                        isRanking = true,
                        rankingNumber = 1, // TODO - api 나오면 수정 (임시 값들)
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
                                text = stringResource(R.string.signature_menu),
                                color = AconTheme.color.White,
                                style = AconTheme.typography.Title4,
                                modifier = Modifier.align(Alignment.Start)
                            )

                            Spacer(Modifier.height(12.dp))
                            SignatureMenu(
                                signatureMenuList = state.spotDetailMenuList // TODO - state.spotDetailInfo.signatureMenuList
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        StoreFloatingButtonSet(
                            onClickMenuBoard = { onClickMenuBoard() },
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
                            onClickBookmark = {
                                //onClickAddBookmark()
                                //onClickDeleteBookmark()
                                // TODO - 북마크 ON 상태이면 북마크 삭제 / 북마크 OFF 상태이면 북마크 추가
                            },
                            isMenuBoarEnabled = true //TODO - state.spotDetailInfo.hasMenuboardImage
                        )
                    }

                    if (storeImageList.size >= 2) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            StoreImageIndicator(
                                pageCount = pageCount,
                                pagerState = pagerState,
                                indicatorScrollState = indicatorScrollState,
                                modifier = Modifier
                                    .padding(top = 16.dp, bottom = 12.dp)
                            )
                        }
                    }

                    AconFilledButton(
                        onClick = { onRequestFindWayModal() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp)
                    ) {
                        Text(
                            text = stringResource(
                                R.string.btn_find_way_walking_time,
                                11
                            ), // TODO - 예상 도착 시간 (도보)
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
            SpotDetailScreen(
                state = SpotDetailUiState.Loading,
                onNavigateToBack = {},
                onFindWayButtonClick = {}
            )
        }
    }
}