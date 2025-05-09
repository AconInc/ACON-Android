package com.acon.acon.feature.verification.screen.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.feature.verification.screen.LocalVerificationSideEffect
import com.acon.acon.feature.verification.screen.LocalVerificationViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LocalVerificationScreenContainer(
    modifier: Modifier = Modifier,
    navigateToSettingsScreen: () -> Unit = {},
    navigateToAreaVerificationToAdd: () -> Unit = {},
    navigateToAreaVerificationToEdit: () -> Unit = {},
    viewModel: LocalVerificationViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()

    LocalVerificationScreen(
        state = state,
        modifier = modifier,
        onNavigateBack = viewModel::onNavigateToSettingsScreen,
        onclickAddArea = viewModel::onNavigateToAreaVerificationAdd,
        onclickEditArea = viewModel::onNavigateToAreaVerificationEdit,
        onDeleteVerifiedAreaChip = viewModel::deleteVerifiedArea,
        onShowEditVerifiedAreaChipDialog = viewModel::onShowEditVerifiedAreaChipDialog,
        onShowDeleteVerifiedAreaChipDialog = viewModel::onShowDeleteVerifiedAreaChipDialog,
    )

    viewModel.collectSideEffect {
        when(it) {
            is LocalVerificationSideEffect.NavigateToSettingsScreen -> navigateToSettingsScreen()
            is LocalVerificationSideEffect.NavigateToAreaVerificationToAdd -> navigateToAreaVerificationToAdd()
            is LocalVerificationSideEffect.NavigateToAreaVerificationToEdit -> navigateToAreaVerificationToEdit()
        }
    }

}