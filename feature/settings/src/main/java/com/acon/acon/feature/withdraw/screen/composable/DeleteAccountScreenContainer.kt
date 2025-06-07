package com.acon.acon.feature.withdraw.screen.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.utils.feature.toast.showToast
import com.acon.acon.core.designsystem.R
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
    val context = LocalContext.current
    val state by viewModel.collectAsState()

    DeleteAccountScreen(
        state = state,
        modifier = modifier,
        onUpdateReason = viewModel::updateReason,
        navigateBack = viewModel::navigateBack,
        onDeleteAccount = viewModel::onDeleteAccount,
        onBottomSheetShowStateChange = viewModel::onDeleteAccountBottomSheetShowStateChange
    )

    viewModel.collectSideEffect {
        when(it) {
            is DeleteAccountSideEffect.ShowToastMessage -> { context.showToast(R.string.toast_delete_account_failed)  }
            is DeleteAccountSideEffect.NavigateToSettings -> navigateToSettings()
            is DeleteAccountSideEffect.NavigateToSignIn -> navigateToSignIn()
        }
    }
}