package com.acon.acon.feature.verification.screen.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.ui.android.showToast
import com.acon.acon.feature.verification.screen.UserVerifiedAreasSideEffect
import com.acon.acon.feature.verification.screen.UserVerifiedAreasViewModel
import com.acon.acon.core.ui.compose.LocalOnRetry
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun UserVerifiedAreasScreenContainer(
    modifier: Modifier = Modifier,
    navigateToSettingsScreen: () -> Unit = {},
    navigateToAreaVerification: (Long) -> Unit = {},
    viewModel: UserVerifiedAreasViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

    CompositionLocalProvider(LocalOnRetry provides viewModel::retry) {
        UserVerifiedAreasScreen(
            state = state,
            modifier = modifier,
            onNavigateBack = viewModel::onNavigateToSettingsScreen,
            onclickAreaVerification = viewModel::onNavigateToAreaVerification,
            onDeleteVerifiedAreaChip = viewModel::deleteVerifiedArea,
            onShowEditAreaDialog = viewModel::showEditAreaDialog,
            onDismissEditAreaDialog = viewModel::dismissEditAreaDialog,
            onDismissDeleteFailDialog = viewModel::dismissAreaDeleteFailDialog
        )
    }

    viewModel.collectSideEffect {
        when (it) {
            is UserVerifiedAreasSideEffect.ShowUnKnownErrorToast -> {
                context.showToast(R.string.unknown_error)
            }

            is UserVerifiedAreasSideEffect.NavigateToSettingsScreen -> navigateToSettingsScreen()
            is UserVerifiedAreasSideEffect.NavigateToAreaVerification-> navigateToAreaVerification(
                it.verifiedAreaId
            )
        }
    }
}