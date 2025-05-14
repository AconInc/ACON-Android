package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.map.onLocationReady
import com.acon.acon.core.utils.feature.permission.CheckAndRequestLocationPermission
import com.acon.acon.core.utils.feature.toast.showToast
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.feature.spot.R
import com.acon.acon.feature.spot.screen.spotlist.SpotListSideEffect
import com.acon.acon.feature.spot.screen.spotlist.SpotListUiState
import com.acon.acon.feature.spot.screen.spotlist.SpotListViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SpotListScreenContainer(
    socialRepository: SocialRepository,
    modifier: Modifier = Modifier,
    onNavigateToAreaVerification: () -> Unit = {},
    onNavigateToSpotDetailScreen: (id: Long) -> Unit = {},
    viewModel: SpotListViewModel = hiltViewModel(),
) {

    val context = LocalContext.current
    val state by viewModel.collectAsState()

    CheckAndRequestLocationPermission(
        onPermissionGranted = {
            if (state !is SpotListUiState.Success) {
                context.onLocationReady(viewModel::fetchSpots)
            }
        }
    )

    SpotListScreen(
        state = state,
        modifier = modifier.fillMaxSize(),
        onRefresh = {
            context.onLocationReady(viewModel::onRefresh)
        },
        onLoginBottomSheetShowStateChange = viewModel::onLoginBottomSheetShowStateChange,
        onFilterBottomSheetShowStateChange = viewModel::onFilterBottomSheetStateChange,
        onResetFilter = {
            context.onLocationReady(viewModel::onResetFilter)
        },
        onCompleteFilter = { condition, proceed ->
            context.onLocationReady {
                viewModel.onCompleteFilter(it, condition, proceed)
            }
        },
        onSpotItemClick = viewModel::onSpotItemClick,
        onGoogleSignIn = {
            context.onLocationReady {
                viewModel.googleLogin(socialRepository, it)
            }
        },
    )

    viewModel.collectSideEffect {
        when (it) {
            is SpotListSideEffect.ShowToastMessage -> { context.showToast(R.string.signin_login_failed_toast) }
            is SpotListSideEffect.NavigateToAreaVerification -> { onNavigateToAreaVerification() }
            is SpotListSideEffect.NavigateToSpotDetail -> { onNavigateToSpotDetailScreen(it.id) }
        }
    }
}