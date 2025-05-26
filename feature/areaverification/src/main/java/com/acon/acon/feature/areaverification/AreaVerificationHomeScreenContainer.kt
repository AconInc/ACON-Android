package com.acon.acon.feature.areaverification

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun AreaVerificationHomeScreenContainer(
    route: String,
    onNextScreen: (Double, Double) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AreaVerificationHomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

    AreaVerificationHomeScreen(
        state = state,
        route = route,
        modifier = modifier,
        onNavigateToBack = {}
    )

    viewModel.collectSideEffect {
        when (it) {
            is AreaVerificationHomeSideEffect.NavigateToBack -> {

            }

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