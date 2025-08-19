package com.acon.acon.feature.areaverification.composable

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.navigation.route.AreaVerificationRoute
import com.acon.acon.core.ui.permission.checkLocationPermission
import com.acon.acon.core.ui.android.showToast
import com.acon.acon.core.ui.compose.LocalRequestLocationPermission
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun AreaVerificationScreenContainer(
    route: String,
    onNextScreen: (Double, Double) -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToSpotList: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AreaVerificationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()
    val onRequestLocationPermission = LocalRequestLocationPermission.current

    AreaVerificationScreen(
        state = state,
        route = route,
        onNextButtonClick = {
            val hasPermission = context.checkLocationPermission()

            if (hasPermission) {
                viewModel.onNextButtonClick()
            } else {
                onRequestLocationPermission()
            }
        },
        modifier = modifier,
        onSkip = viewModel::onSkipButtonClick
    )

    viewModel.useLiveLocation()
    viewModel.collectSideEffect {
        when (it) {
            is AreaVerificationSideEffect.NavigateToAppLocationSettings -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", it.packageName, null)
                }
                context.startActivity(intent)
            }

            is AreaVerificationSideEffect.NavigateToSystemLocationSettings -> {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                    data = Uri.fromParts("package", it.packageName, null)
                }
                context.startActivity(intent)
            }

            is AreaVerificationSideEffect.NavigateToNextScreen -> {
                onNextScreen(it.latitude, it.longitude)
            }

            is AreaVerificationSideEffect.ShowErrorToast -> {
                context.showToast(it.errorMessage)
            }

            is AreaVerificationSideEffect.NavigateToOnboarding -> onNavigateToOnboarding()
            is AreaVerificationSideEffect.NavigateToSpotList -> onNavigateToSpotList()
        }
    }
}