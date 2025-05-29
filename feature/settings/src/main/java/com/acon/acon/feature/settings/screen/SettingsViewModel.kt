package com.acon.acon.feature.settings.screen

import androidx.lifecycle.viewModelScope
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.repository.TokenRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.type.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
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
        container<SettingsUiState, SettingsSideEffect>(SettingsUiState.Guest) {
            userRepository.getUserType().collectLatest { userType ->
                when (userType) {
                    UserType.GUEST -> reduce { SettingsUiState.Guest }
                    UserType.USER -> reduce { SettingsUiState.User() }
                    UserType.ADMIN -> reduce { SettingsUiState.User() }
                }
            }
        }

    fun onSignOut() = intent {
        onLogoutDialogShowStateChange(false)
        tokenRepository.getRefreshToken().onSuccess { refreshToken ->
            viewModelScope.launch {
                refreshToken?.let {
                    userRepository.logout(it)
                }
                ?.onSuccess {
                    postSideEffect(SettingsSideEffect.NavigateToSignIn)
                }
                ?.onFailure {
                    postSideEffect(SettingsSideEffect.ShowToastMessage)
                }
            }
        }
    }

    fun onLogoutDialogShowStateChange(show: Boolean) = intent {
        runOn<SettingsUiState.User> {
            reduce { state.copy(showLogOutDialog = show) }
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
        amplitudeRetryOnboarding()
    }

    fun onNavigateToLocalVerification() = intent {
        postSideEffect(SettingsSideEffect.NavigateToLocalVerification)
    }

    fun onDeleteAccount() = intent {
        postSideEffect(SettingsSideEffect.NavigateToDeleteAccount)
    }
}

sealed interface SettingsUiState{
    data class User(
        val showLogOutDialog: Boolean = false
    ) : SettingsUiState

    data object Guest : SettingsUiState
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