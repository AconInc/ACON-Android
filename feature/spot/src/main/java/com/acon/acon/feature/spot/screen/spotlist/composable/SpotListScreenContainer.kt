package com.acon.acon.feature.spot.screen.spotlist.composable

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.map.onLocationReady
import com.acon.acon.core.utils.feature.constants.AppURL
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
                context.onLocationReady {
                    viewModel.fetchInitialSpots(it)
                }
            }
        }
    )

    SpotListScreen(
        state = state,
        modifier = modifier.fillMaxSize(),
        onRefresh = {
            context.onLocationReady {
                viewModel.onRefresh(it)
            }
        },
        onLoginBottomSheetShowStateChange = viewModel::onLoginBottomSheetShowStateChange,
        onFilterBottomSheetShowStateChange = viewModel::onFilterBottomSheetStateChange,
        onResetFilter = {
            context.onLocationReady {
                viewModel.onResetFilter(it)
            }
        },
        onCompleteFilter = { condition, proceed ->
            context.onLocationReady {
                viewModel.onCompleteFilter(it, condition, proceed)
            }
        },
        onSpotItemClick = viewModel::onSpotItemClick,
        onTermOfUse = viewModel::onTermOfUse,
        onPrivatePolicy = viewModel::onPrivatePolicy,
        onGoogleSignIn = { viewModel.googleLogin(socialRepository) },
    )

    viewModel.collectSideEffect {
        when (it) {
            is SpotListSideEffect.ShowToastMessage -> { context.showToast(R.string.signin_login_failed_toast) }
            is SpotListSideEffect.NavigateToAreaVerification -> { onNavigateToAreaVerification() }
            is SpotListSideEffect.NavigateToSpotDetail -> { onNavigateToSpotDetailScreen(it.id) }
            is SpotListSideEffect.OnTermOfUse -> {
                val url = AppURL.TERM_OF_USE
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
            is SpotListSideEffect.OnPrivatePolicy -> {
                val url = AppURL.PRIVATE_POLICY
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        }
    }
}