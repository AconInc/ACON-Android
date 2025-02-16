package com.acon.acon.feature.withdraw.screen.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.feature.withdraw.screen.DeleteAccountSideEffect
import com.acon.acon.feature.withdraw.screen.DeleteAccountViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun DeleteAccountScreenContainer(
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit = {},
    navigateToSignIn: () -> Unit = {},
    viewModel: DeleteAccountViewModel = hiltViewModel()

) {
    val state by viewModel.collectAsState()

    DeleteAccountScreen(
        state = state,
        modifier = modifier,
        navigateBack = viewModel::navigateBack,
        onDeleteAccount = viewModel::onDeleteAccount,
        onBottomSheetShowStateChange = viewModel::onDeleteAccountBottomSheetShowStateChange
    )

    viewModel.collectSideEffect {
        when(it) {
            is DeleteAccountSideEffect.NavigateToSettings -> navigateToSettings()
            is DeleteAccountSideEffect.NavigateToSignIn -> navigateToSignIn()
        }
    }
}