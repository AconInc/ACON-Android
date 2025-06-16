package com.acon.acon.feature.spot.screen.spotdetail.composable

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.acon.acon.core.common.UrlConstants
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.component.error.NetworkErrorView
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.imageGradientLayer
import com.acon.acon.core.designsystem.image.rememberDefaultLoadImageErrorPainter
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.spot.screen.component.OperationDot
import com.acon.acon.feature.spot.screen.spotdetail.createBranchDeepLink
import com.acon.feature.common.compose.LocalOnRetry
import com.acon.feature.common.compose.getTextSizeDp
import dev.chrisbanes.haze.hazeSource
import okhttp3.internal.immutableListOf

@Composable
internal fun SpotDetailScreen(
    state: SpotDetailUiState,
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit = {},
    onClickBookmark: () -> Unit = {},
    onClickRequestMenuBoard: () -> Unit = {},
    onDismissMenuBoard: () -> Unit = {},
    onRequestErrorReportModal: () -> Unit = {},
    onDismissErrorReportModal: () -> Unit = {},
    onRequestFindWayModal: () -> Unit = {},
    onDismissFindWayModal: () -> Unit = {},
    onClickFindWay: () -> Unit = {}
) {
    val context = LocalContext.current
    val indicatorScrollState = rememberLazyListState()

    val noStoreText = immutableListOf(
        stringResource(R.string.no_store_image_verified),
        stringResource(R.string.no_store_image_secret),
        stringResource(R.string.no_store_image_mystery)
    )

    when (state) {
        is SpotDetailUiState.LoadFailed -> {
            NetworkErrorView(
                onRetry = LocalOnRetry.current,
                modifier = Modifier.fillMaxSize()
            )
        }

        is SpotDetailUiState.Loading -> {}
        is SpotDetailUiState.Success -> {
            Log.d("로그", "딥링크 진입 : ${state.isFromDeepLink}")
            val storeName = state.spotDetail.name
            val storeImageList = state.spotDetail.imageList
            val acornCount = state.spotDetail.acornCount
            val isStoreOpen = state.spotDetail.isOpen

            val bottomPadding = if (storeImageList.size <= 1) {
                34.dp
            } else {
                0.dp
            }

            val pageCount = state.spotDetail.imageList.size
            val pagerState = rememberPagerState(initialPage = 0, pageCount = { pageCount })

            Box(
                modifier = modifier
                    .navigationBarsPadding()
                    .statusBarsPadding()
            ) {
                if (state.showReportErrorModal) {
                    ReportErrorBottomSheet(
                        onDismissRequest = { onDismissErrorReportModal() },
                        onClickReportError = {
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(UrlConstants.ERROR_REPORT))
                            context.startActivity(intent)
                        }
                    )
                }

                if (state.showFindWayModal) {
                    // TODO - 프로필, 북마크,딥링크 진입 유저 - 길찾기 방식 -> route/public
                    FindWayBottomSheet(
                        onFindWay = {
                            onClickFindWay()
                            onDismissFindWayModal()
                        },
                        onDismissRequest = { onDismissFindWayModal() }
                    )
                }

                if (state.showMenuBoardDialog) {
                    MenuBoardOverlay(
                        imageList = state.menuBoardList,
                        isMenuBoardLoaded = state.menuBoardListLoad,
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
                                ),
                            error = rememberDefaultLoadImageErrorPainter()
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
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .hazeSource(LocalHazeState.current)
                ) {
                    AconTopBar(
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
                        },
                        modifier = Modifier.padding(vertical = 14.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = storeName,
                            style = AconTheme.typography.Title3,
                            color = AconTheme.color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(Modifier.width(40.dp))
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.acon_line),
                            contentDescription = stringResource(R.string.acorn_count_content_description),
                            tint = AconTheme.color.Gray50
                        )
                        Text(
                            text = when {
                                acornCount > 9999 -> stringResource(R.string.over_max_acon_count)
                                acornCount == 0 -> ""
                                else -> acornCount.toString()
                            },
                            style = AconTheme.typography.Body1,
                            color = AconTheme.color.White,
                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .padding(start = 2.dp)
                                .widthIn(
                                    max = getTextSizeDp(
                                        "+9999",
                                        AconTheme.typography.Body1
                                    ).width
                                ),
                            textAlign = TextAlign.End
                        )
                    }

                    /*  TODO - 장소 상세 Tag 처리 로직
                         * 일반 유저: 이전 페이지 "NEW", "LOCAL", "TOP" 태그 그대로 가져오기
                         * 프로필, 북마크, 딥링크로 진입한 유저: API 응답으로 제공
                     */
                    Spacer(Modifier.height(8.dp))
                    StoreTagRow(
                        tags = state.tags ?: emptyList(),
                        modifier = Modifier.padding(start = 20.dp)
                    )

                    Row(
                        modifier = Modifier.padding(start = 20.dp, top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OperationDot(state.spotDetail.isOpen)

                        Text(
                            text = if (isStoreOpen) state.spotDetail.closingTime else state.spotDetail.nextOpening,
                            color = AconTheme.color.Gray200,
                            style = AconTheme.typography.Body1,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 12.dp)
                        )

                        Text(
                            text = stringResource(R.string.store_closed),
                            color = AconTheme.color.Gray200,
                            style = AconTheme.typography.Body1,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

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
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.align(Alignment.Start)
                            )

                            Spacer(Modifier.height(12.dp))
                            SignatureMenu(
                                signatureMenuList = state.spotDetail.signatureMenuList
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        StoreFloatingButtonSet(
                            onClickMenuBoard = { onClickRequestMenuBoard() },

                            // TODO - 딥링크 동작 확인용 임시 코드, 수정 필요
                            onClickShare = {
                                val image = storeImageList.getOrElse(0) { "" }
                                val spotId = state.spotDetail.spotId
                                val spotName = state.spotDetail.name

                                createBranchDeepLink(
                                    context = context,
                                    spotId = spotId,
                                    spotName = spotName,
                                    imageUrl = image.takeIf { it.isNotBlank() }
                                ) { branchLink ->
                                    val shareIntent = Intent.createChooser(
                                        Intent().apply {
                                            action = Intent.ACTION_SEND
                                            type =
                                                if (image.isNotBlank()) "image/*" else "text/plain"
                                            putExtra(
                                                Intent.EXTRA_TEXT,
                                                "아콘에서 내 근처 ${spotName} 확인해보세요!\n$branchLink"
                                            )
                                            if (image.isNotBlank()) {
                                                putExtra(Intent.EXTRA_STREAM, image)
                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            }
                                        },
                                        null
                                    )
                                    context.startActivity(shareIntent)
                                }
                            },
                            onClickBookmark = {
                                onClickBookmark()
                            },
                            isBookmarkSelected = state.isBookmarkSaved,
                            isMenuBoarEnabled = state.spotDetail.hasMenuboardImage
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
                        onClick = {
                            onRequestFindWayModal()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp)
                    ) {
                        // TODO - 프로필, 북마크,딥링크로 들어온 유저 버튼명 -> 그냥 길찾기
                        Text(
                            text = if (state.eta == null) {
                                ""
                            } else {
                                stringResource(
                                    R.string.btn_find_way_walking_time,
                                    state.getTransportLabel(),
                                    state.eta
                                )
                            },
                            color = AconTheme.color.White,
                            style = AconTheme.typography.Title4,
                            fontWeight = FontWeight.SemiBold
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
                onClickFindWay = {}
            )
        }
    }
}