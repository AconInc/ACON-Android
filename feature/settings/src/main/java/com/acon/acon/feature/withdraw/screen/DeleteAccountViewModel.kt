package com.acon.acon.feature.withdraw.screen

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.repository.TokenRepository
import com.acon.acon.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository
) : BaseContainerHost<DeleteAccountUiState, DeleteAccountSideEffect>() {

    override val container =
        container<DeleteAccountUiState, DeleteAccountSideEffect>(DeleteAccountUiState.Default()) { }

    fun onDeleteAccount() = intent {
        runOn<DeleteAccountUiState.Default> {
            val reason = state.reason

            viewModelScope.launch {
                tokenRepository.getRefreshToken()
                    .onSuccess { refreshToken ->
                        refreshToken?.let { token ->
                            userRepository.deleteAccount(reason, token)
                                .onSuccess {
                                    tokenRepository.removeAllToken()
                                    postSideEffect(DeleteAccountSideEffect.NavigateToSignIn)
                                }
                                .onFailure {
                                    onDeleteAccountBottomSheetShowStateChange(false)
                                    postSideEffect(DeleteAccountSideEffect.ShowToastMessage)
                                }
                        }
                    }
            }
        }
    }

    fun updateReason(reason: String) = intent {
        runOn<DeleteAccountUiState.Default> {
            reduce { state.copy(reason = reason) }
        }
    }

    fun navigateBack() = intent {
        postSideEffect(DeleteAccountSideEffect.NavigateToSettings)
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
        val reason: String = "",
        val showDeleteAccountBottomSheet: Boolean = false
    ) : DeleteAccountUiState
}

sealed interface DeleteAccountSideEffect {
    data object ShowToastMessage : DeleteAccountSideEffect
    data object NavigateToSettings : DeleteAccountSideEffect
    data object NavigateToSignIn : DeleteAccountSideEffect
}
