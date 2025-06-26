package com.acon.acon.feature.spot.screen.spotdetail.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.map.onLocationReady
import com.acon.acon.core.utils.feature.toast.showToast
import com.acon.core.ui.compose.LocalOnRetry
import com.acon.feature.common.intent.openNaverMapNavigationWithMode
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SpotDetailScreenContainer(
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit = {},
    onBackToAreaVerification: () -> Unit = {},
    viewModel: SpotDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

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

    viewModel.useUserType()
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
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