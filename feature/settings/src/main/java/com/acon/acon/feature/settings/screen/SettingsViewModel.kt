package com.acon.acon.feature.settings.screen

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
class SettingsViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository
) : BaseContainerHost<SettingsUiState, SettingsSideEffect>() {

    override val container =
        container<SettingsUiState, SettingsSideEffect>(SettingsUiState.Default(false)) {
            userRepository.getLoginState().collect { loginState ->
                reduce { SettingsUiState.Default(loginState) }
            }
        }

    fun onSingOut() = intent {
        tokenRepository.getRefreshToken().onSuccess { refreshToken ->
            viewModelScope.launch {
                refreshToken?.let { userRepository.postLogout(it) }
                    ?.onSuccess {
                        userRepository.updateLoginState(false)
                        postSideEffect(SettingsSideEffect.NavigateToSignIn)
                    }
                    ?.onFailure {
                        onSignInDialogShowStateChange(false)
                        postSideEffect(SettingsSideEffect.ShowToastMessage)
                    }
            }
        }
    }

     fun onSignInDialogShowStateChange(show: Boolean) = intent {
        runOn<SettingsUiState.Default> {
            reduce { state.copy(onSignInDialogShowStateChange = show) }
        }
    }

    fun navigateBack() = intent {
        postSideEffect(SettingsSideEffect.NavigateToProfile)
    }

    fun onUpdateVersion() = intent {
        postSideEffect(SettingsSideEffect.OpenPlayStore)
    }

    fun onTermOfUse() = intent {
        postSideEffect(SettingsSideEffect.OpenTermOfUse)
    }

    fun onPrivatePolicy() = intent {
        postSideEffect(SettingsSideEffect.OpenPrivatePolicy)
    }

    fun onRetryOnBoarding() = intent {
        postSideEffect(SettingsSideEffect.NavigateToOnboarding)
    }

    fun onNavigateToLocalVerification() = intent {
        postSideEffect(SettingsSideEffect.NavigateToLocalVerification)
    }

    fun onDeleteAccount() = intent {
        postSideEffect(SettingsSideEffect.NavigateToDeleteAccount)
    }
}

sealed interface SettingsUiState{
    data class Default(
        val isLogin: Boolean = false,
        val onSignInDialogShowStateChange: Boolean = false
    ) : SettingsUiState
}

sealed interface SettingsSideEffect {
    data object ShowToastMessage : SettingsSideEffect
    data object NavigateToSignIn : SettingsSideEffect
    data object NavigateToProfile : SettingsSideEffect
    data object OpenPlayStore : SettingsSideEffect
    data object OpenTermOfUse : SettingsSideEffect
    data object OpenPrivatePolicy : SettingsSideEffect
    data object NavigateToOnboarding : SettingsSideEffect
    data object NavigateToLocalVerification : SettingsSideEffect
    data object NavigateToDeleteAccount : SettingsSideEffect
}