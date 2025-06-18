package com.acon.acon.feature.spot.screen.spotdetail.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.map.onLocationReady
import com.acon.acon.core.utils.feature.toast.showToast
import com.acon.feature.common.compose.LocalDeepLinkHandler
import com.acon.feature.common.compose.LocalOnRetry
import com.acon.feature.common.intent.openNaverMapNavigationWithMode
import kotlinx.coroutines.flow.filter
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import timber.log.Timber

@Composable
fun SpotDetailScreenContainer(
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit = {},
    onBackToAreaVerification: () -> Unit = {},
    viewModel: SpotDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

    val deepLinkHandler = LocalDeepLinkHandler.current

    // 딥링크로 접속한 경우에만 인증 지역 조회 api 호출
    LaunchedEffect(Unit) {
        snapshotFlow { deepLinkHandler.hasDeepLink.value }
            .filter { it }
            .collect {
                Timber.tag("SpotDetailScreenContainer").d("isAreaVerified 호출")
                viewModel.isAreaVerified()
            }
    }

    CompositionLocalProvider(LocalOnRetry provides viewModel::retry) {
        SpotDetailScreen(
            state = state,
            modifier = modifier,
            onNavigateToBack = viewModel::navigateToBack,
            onBackToAreaVerification = onBackToAreaVerification,
            onClickBookmark = viewModel::toggleBookmark,
            onClickRequestMenuBoard = viewModel::fetchMenuBoardList,
            onDismissMenuBoard = viewModel::onDismissMenuBoard,
            onRequestErrorReportModal = viewModel::onRequestReportErrorModal,
            onDismissErrorReportModal = viewModel::onDismissReportErrorModal,
            onRequestFindWayModal = viewModel::onRequestFindWayModal,
            onDismissFindWayModal = viewModel::onDismissFindWayModal,
            onClickFindWay = viewModel::fetchRecentNavigationLocation
        )
    }

    viewModel.emitUserType()
    viewModel.collectSideEffect { sideEffect ->
        when(sideEffect) {
            is SpotDetailSideEffect.NavigateToBack -> {
                onNavigateToBack()
            }
            is SpotDetailSideEffect.RecentLocationFetched -> {
                context.onLocationReady { location ->
                    viewModel.onFindWay(location)
                }
            }
            is SpotDetailSideEffect.OnFindWayButtonClick -> {
                context.openNaverMapNavigationWithMode(
                    start = sideEffect.start,
                    destination = sideEffect.destination,
                    destinationName = sideEffect.destinationName,
                    transportMode = sideEffect.transportMode,
                    isPublic = sideEffect.isPublic
                )
            }
            is SpotDetailSideEffect.ShowErrorToast -> context.showToast(R.string.unknown_error_message)
        }
    }
}