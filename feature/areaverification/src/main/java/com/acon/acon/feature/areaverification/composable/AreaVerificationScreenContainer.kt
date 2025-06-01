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

    AreaVerificationScreen(
        state = state,
        route = route,
        onPermissionSettingClick = { viewModel.onPermissionSettingClick(context.packageName) },
        onNextButtonClick = {
            val hasPermission = context.checkLocationPermission()
            viewModel.updateLocationPermissionStatus(hasPermission)

            if (hasPermission) {
                viewModel.onNextButtonClick()
            } else {
                viewModel.showPermissionDialog()
            }
        },
        modifier = modifier
    )

    viewModel.collectSideEffect {
        when (it) {
            is AreaVerificationHomeSideEffect.NavigateToAppLocationSettings -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", it.packageName, null)
                }
                context.startActivity(intent)
            }

            is AreaVerificationHomeSideEffect.NavigateToSystemLocationSettings -> {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                    data = Uri.fromParts("package", it.packageName, null)
                }
                context.startActivity(intent)
            }

            is AreaVerificationHomeSideEffect.NavigateToNextScreen -> {
                onNextScreen(it.latitude, it.longitude)
            }
        }
    }
}