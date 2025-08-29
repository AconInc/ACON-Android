package com.acon.feature.onboarding.area.composable

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.navigation.LocalNavController
import com.acon.acon.core.ui.android.showToast
import com.acon.acon.core.ui.compose.LocalRequestLocationPermission
import com.acon.acon.core.ui.permission.checkLocationPermission
import com.acon.feature.onboarding.area.viewmodel.AreaVerificationSideEffect
import com.acon.feature.onboarding.area.viewmodel.AreaVerificationViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun AreaVerificationScreenContainer(
    onNavigateToVerifyInMap: () -> Unit,
    onNavigateToChooseDislikes: () -> Unit,
    backGestureEnabled: Boolean,
    modifier: Modifier = Modifier,
    viewModel: AreaVerificationViewModel = hiltViewModel(creationCallback = { factory: AreaVerificationViewModel.Factory ->
        factory.create(shouldShowSkipButton = backGestureEnabled.not())
    })
) {
    val context = LocalContext.current
    val onRequestLocationPermission = LocalRequestLocationPermission.current
    val state by viewModel.collectAsState()

    AreaVerificationScreen(
        state = state,
        onNextButtonClick = {
            val hasPermission = context.checkLocationPermission()

            if (hasPermission) {
                viewModel.onNextButtonClick()
            } else {
                onRequestLocationPermission()
            }
        },
        modifier = modifier,
        onSkipClick = viewModel::onSkipClicked
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

            is AreaVerificationSideEffect.NavigateToVerifyInMap -> {
                onNavigateToVerifyInMap()
            }

            is AreaVerificationSideEffect.ShowErrorToast -> {
                context.showToast(it.errorMessage)
            }

            is AreaVerificationSideEffect.NavigateToChooseDislikes -> onNavigateToChooseDislikes()
        }
    }

    val navController = LocalNavController.current
    BackHandler {
        if (backGestureEnabled) {
            navController.popBackStack()
        }
    }
}