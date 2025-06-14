package com.acon.acon.feature.verification.screen.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.utils.feature.toast.showToast
import com.acon.acon.feature.verification.screen.LocalVerificationSideEffect
import com.acon.acon.feature.verification.screen.LocalVerificationViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LocalVerificationScreenContainer(
    modifier: Modifier = Modifier,
    navigateToSettingsScreen: () -> Unit = {},
    navigateToAreaVerification: (Long) -> Unit = {},
    viewModel: LocalVerificationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

    LocalVerificationScreen(
        state = state,
        modifier = modifier,
        onNavigateBack = viewModel::onNavigateToSettingsScreen,
        onclickAreaVerification = viewModel::onNavigateToAreaVerification,
        onDeleteVerifiedAreaChip = viewModel::deleteVerifiedArea,
        onShowEditAreaDialog = viewModel::showEditAreaDialog,
        onDismissEditAreaDialog = viewModel::dismissEditAreaDialog,
        onDismissDeleteFailDialog = viewModel::dismissAreaDeleteFailDialog
    )

    viewModel.collectSideEffect {
        when (it) {
            is LocalVerificationSideEffect.ShowUnKnownErrorToast -> {
                context.showToast(R.string.unknown_error)
            }

            is LocalVerificationSideEffect.NavigateToSettingsScreen -> navigateToSettingsScreen()
            is LocalVerificationSideEffect.NavigateToAreaVerification-> navigateToAreaVerification(
                it.verifiedAreaId
            )
        }
    }
}