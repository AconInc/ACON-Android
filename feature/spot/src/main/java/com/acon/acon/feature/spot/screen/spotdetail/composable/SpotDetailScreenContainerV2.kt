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
fun SpotDetailScreenContainerV2(
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit = {},
    viewModel: SpotDetailViewModelV2 = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

    SpotDetailScreenV2(
        state = state,
        modifier = modifier,
        onNavigateToBack = viewModel::navigateToBack,
        onFindWayButtonClick = {
            viewModel.fetchRecentNavigationLocation()
        },
    )

    viewModel.collectSideEffect { sideEffect ->
        when(sideEffect) {
            is SpotDetailSideEffectV2.NavigateToBack -> {
                onNavigateToBack()
            }
            is SpotDetailSideEffectV2.RecentLocationFetched -> {
                context.onLocationReady { location ->
                    viewModel.onFindWay(location)
                }
            }
            is SpotDetailSideEffectV2.RecentLocationFetchFailed -> {
                // TODO -> 최근 길 안내 장소 저장 (실패)
            }
            is SpotDetailSideEffectV2.OnFindWayButtonClick -> {
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