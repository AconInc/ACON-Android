package com.acon.acon.feature.spot.screen.spotdetail.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.map.onLocationReady
import com.acon.acon.feature.openNaverMap
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SpotDetailScreenContainer(
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit = {},
    viewModel: SpotDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

    SpotDetailScreen(
        state = state,
        modifier = modifier,
        onClickAddBookmark = {}, //TODO - 북마크 추가 api
        onClickDeleteBookmark = {}, //TODO - 북마크 삭제 api
        onNavigateToBack = viewModel::navigateToBack,
        onClickMenuBoard = viewModel::onRequestMenuBoard,
        onClickRefreshMenuBoard = viewModel::fetchMenuBoardList,
        onDismissMenuBoard = viewModel::onDismissMenuBoard,
        onRequestErrorReportModal = viewModel::onRequestReportErrorModal,
        onDismissErrorReportModal = viewModel::onDismissReportErrorModal,
        onRequestFindWayModal = viewModel::onRequestFindWayModal,
        onDismissFindWayModal = viewModel::onDismissFindWayModal,
        onFindWayButtonClick = {
            viewModel.fetchRecentNavigationLocation()
        },
    )

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
            is SpotDetailSideEffect.RecentLocationFetchFailed -> {
                // TODO -> 최근 길 안내 장소 저장 (실패)
            }
            is SpotDetailSideEffect.OnFindWayButtonClick -> {
                openNaverMap(
                    context = context,
                    location = sideEffect.startLocation,
                    destinationLat = sideEffect.goalDestinationLat,
                    destinationLng = sideEffect.goalDestinationLng,
                    destinationName = sideEffect.goalDestinationName
                )
            }
        }
    }
}