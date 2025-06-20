package com.acon.acon.feature.spot.screen.spotdetail.composable

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.acon.acon.core.designsystem.component.error.TopbarNetworkErrorView
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.imageGradientLayer
import com.acon.acon.core.designsystem.image.rememberDefaultLoadImageErrorPainter
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.type.UserType
import com.acon.acon.feature.spot.screen.component.OperationDot
import com.acon.acon.feature.spot.screen.spotdetail.createBranchDeepLink
import com.acon.acon.feature.spot.screen.spotlist.composable.SpotDetailLoadingView
import com.acon.core.analytics.amplitude.AconAmplitude
import com.acon.core.analytics.constants.EventNames
import com.acon.core.analytics.constants.PropertyKeys
import com.acon.feature.common.compose.LocalDeepLinkHandler
import com.acon.feature.common.compose.LocalOnRetry
import com.acon.feature.common.compose.LocalRequestSignIn
import com.acon.feature.common.compose.LocalUserType
import com.acon.feature.common.compose.getTextSizeDp
import dev.chrisbanes.haze.hazeSource
import okhttp3.internal.immutableListOf

@Composable
internal fun SpotDetailScreen(
    state: SpotDetailUiState,
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit = {},
    onBackToAreaVerification: () -> Unit = {},
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

    val userType = LocalUserType.current
    val deepLinkHandler = LocalDeepLinkHandler.current
    val onSignInRequired = LocalRequestSignIn.current

    when (state) {
        is SpotDetailUiState.LoadFailed -> {
            TopbarNetworkErrorView(
                onRetry = LocalOnRetry.current,
                onNavBack = { onNavigateToBack() },
                modifier = Modifier
                    .background(AconTheme.color.Gray900)
                    .fillMaxSize()
            )
        }

        is SpotDetailUiState.Loading -> {
            SpotDetailLoadingView(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AconTheme.color.Gray900)
            )
        }

        is SpotDetailUiState.Success -> {
            BackHandler {
                if (state.isAreaVerified) {
                    deepLinkHandler.clear()
                    onNavigateToBack()
                } else if (deepLinkHandler.hasDeepLink.value
                    && userType == UserType.USER
                ) {
                    deepLinkHandler.clear()
                    onBackToAreaVerification()
                } else {
                    deepLinkHandler.clear()
                    onNavigateToBack()
                }
            }

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
            ) {
                if (state.showReportErrorModal) {
                    ReportErrorBottomSheet(
                        onDismissRequest = { onDismissErrorReportModal() },
                        onClickReportError = {
                            AconAmplitude.trackEvent(
                                eventName = EventNames.DETAIL_PAGE,
                                property = PropertyKeys.CLICK_REPORT_ERROR to true
                            )
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(UrlConstants.ERROR_REPORT))
                            context.startActivity(intent)
                        }
                    )
                }

                if (state.showFindWayModal) {
                    // 프로필, 북마크,딥링크 진입 유저 - 길찾기 방식 -> route/public
                    FindWayBottomSheet(
                        onFindWay = {
                            AconAmplitude.trackEvent(
                                eventName = EventNames.DETAIL_PAGE,
                                property = PropertyKeys.CLICK_DETAIL_NAVIGATION to true
                            )
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
                                .hazeSource(LocalHazeState.current)
                                .fillMaxSize()
                                .imageGradientLayer(
                                    startColor = AconTheme.color.Gray900.copy(alpha = 0.8f),
                                    ratio = 0.5f
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
                                .fillMaxSize(),
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
                        .hazeSource(LocalHazeState.current)
                        .fillMaxSize()
                        .navigationBarsPadding()
                ) {
                    AconTopBar(
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    if (state.isAreaVerified) {
                                        deepLinkHandler.clear()
                                        onNavigateToBack()
                                    } else if (deepLinkHandler.hasDeepLink.value
                                        && userType == UserType.USER
                                    ) {
                                        deepLinkHandler.clear()
                                        onBackToAreaVerification()
                                    } else {
                                        deepLinkHandler.clear()
                                        onNavigateToBack()
                                    }
                                }
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
                        modifier = Modifier
                            .padding(top = 44.dp, bottom = 14.dp)
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

                    // 장소 상세 Tag 처리 로직
                    // 일반 유저: 이전 페이지 "NEW", "LOCAL", "TOP" 태그 그대로 가져오기
                    // 프로필, 북마크, 딥링크로 진입한 유저: API 응답으로 제공
                    Spacer(Modifier.height(8.dp))
                    StoreTagRow(
                        tags = state.storeTags,
                        modifier = Modifier.padding(start = 20.dp)
                    )

                    Row(
                        modifier = Modifier.padding(start = 26.dp, top = 8.dp),
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
                            onClickShare = {
                                AconAmplitude.trackEvent(
                                    eventName = EventNames.DETAIL_PAGE,
                                    property = PropertyKeys.CLICK_SHARE to true
                                )
                                createBranchDeepLink(
                                    context = context,
                                    spotId = state.spotDetail.spotId,
                                    spotName = state.spotDetail.name
                                ) { branchLink ->
                                    val shareIntent = Intent.createChooser(
                                        Intent().apply {
                                            action = Intent.ACTION_SEND
                                            type = "text/plain"
                                            putExtra(
                                                Intent.EXTRA_TEXT,
                                                "Acon에서 내 근처 ${state.spotDetail.name} 확인해보세요!$branchLink"
                                            )
                                        },
                                        null
                                    )
                                    context.startActivity(shareIntent)
                                }
                            },
                            onClickBookmark = {
                                if (state.isFromDeepLink == true && userType == UserType.GUEST) {
                                    onSignInRequired("")
                                } else {
                                    onClickBookmark()
                                }
                            },
                            isBookmarkSelected = if (state.isFromDeepLink == true && userType == UserType.GUEST) false else state.spotDetail.isSaved,
                            isMenuBoardEnabled = state.spotDetail.hasMenuboardImage
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
                            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AconTheme.color.Gray900.copy(alpha = .8f),
                            contentColor = AconTheme.color.White,
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    AconTheme.color.PrimaryDefault,
                                    Color(0xFF4CBD01),
                                    AconTheme.color.White
                                ),
                                startX = 0f,
                                endX = Float.POSITIVE_INFINITY
                            )
                        )
                    ) {
                        if (state.navFromProfile == true || state.isFromDeepLink == true) {
                            Text(
                                text = stringResource(R.string.btn_find_way),
                                color = AconTheme.color.White,
                                style = AconTheme.typography.Title4,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
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