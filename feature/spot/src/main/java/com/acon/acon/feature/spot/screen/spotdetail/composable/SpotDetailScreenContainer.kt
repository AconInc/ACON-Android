package com.acon.acon.feature.spot.screen.spotdetail.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.map.onLocationReady
import com.acon.acon.feature.openNaverMap
import com.acon.acon.feature.spot.screen.spotdetail.SpotDetailSideEffect
import com.acon.acon.feature.spot.screen.spotdetail.SpotDetailViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SpotDetailScreenContainer(
    onNavigateToSpotListView: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SpotDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

//    SpotDetailScreen(
//        state = state,
//        modifier = modifier.fillMaxSize(),
//        onNavigateToSpotListView = viewModel::navigateToSpotListView,
//        onFindWayButtonClick = { ->
//            viewModel.fetchRecentNavigationLocation()
//        },
//    )

    //SpotDetailScreenV2()

    viewModel.collectSideEffect { sideEffect ->
        when(sideEffect) {
            is SpotDetailSideEffect.NavigateToSpotListView -> {
                onNavigateToSpotListView()
            }
            is SpotDetailSideEffect.RecentLocationFetched -> {
                context.onLocationReady {
                    viewModel.onFindWay(it)
                }
            }
            is SpotDetailSideEffect.RecentLocationFetchFailed -> {
                // TODO -> 최근 길 안내 장소 저장 (실패)
            }
            is SpotDetailSideEffect.OnFindWayButtonClick -> {
                openNaverMap(
                    context = context,
                    location = sideEffect.location,
                    destinationLat = sideEffect.destinationLat,
                    destinationLng = sideEffect.destinationLng,
                    destinationName = sideEffect.destinationName
                )
            }
        }
    }

}