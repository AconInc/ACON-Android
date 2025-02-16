package com.acon.acon.feature.withdraw.screen

import androidx.compose.runtime.Immutable
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class DeleteAccountViewModel @Inject constructor(

) : BaseContainerHost<DeleteAccountUiState, DeleteAccountSideEffect>() {

    override val container =
        container<DeleteAccountUiState, DeleteAccountSideEffect>(DeleteAccountUiState.Default()) { }

    fun navigateBack() = intent {
        postSideEffect(DeleteAccountSideEffect.NavigateToSettings)
    }

    fun onDeleteAccount() = intent {
        postSideEffect(DeleteAccountSideEffect.NavigateToSignIn)
    }

    fun onDeleteAccountBottomSheetShowStateChange(show: Boolean) = intent {
        runOn<DeleteAccountUiState.Default> {
            reduce {
                state.copy(showDeleteAccountBottomSheet = show)
            }
        }
    }
}

sealed interface DeleteAccountUiState {
    @Immutable
    data class Default(
        val showDeleteAccountBottomSheet: Boolean = false
    ) : DeleteAccountUiState
}

sealed interface DeleteAccountSideEffect {
    data object NavigateToSettings : DeleteAccountSideEffect
    data object NavigateToSignIn : DeleteAccountSideEffect
}
