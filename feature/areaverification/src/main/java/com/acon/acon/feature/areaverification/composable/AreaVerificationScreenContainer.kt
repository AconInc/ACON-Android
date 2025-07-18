package com.acon.acon.feature.areaverification.composable

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.utils.feature.permission.checkLocationPermission
import com.acon.acon.core.utils.feature.toast.showToast
import com.acon.feature.common.compose.LocalRequestLocationPermission
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun AreaVerificationScreenContainer(
    route: String,
    onNextScreen: (Double, Double) -> Unit,
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
        modifier = modifier
    )

    viewModel.emitLiveLocation()
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
        }
    }
}