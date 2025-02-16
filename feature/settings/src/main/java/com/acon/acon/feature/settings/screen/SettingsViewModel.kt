package com.acon.acon.feature.settings.screen

import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    // TODO - 로그아웃 추가 해야 함
    private val authRepository: AuthRepository
) : BaseContainerHost<SettingsUiState, SettingsSideEffect>() {

    override val container =
        container<SettingsUiState, SettingsSideEffect>(SettingsUiState.Default(false)) {
            authRepository.getLoginState().collect { loginState ->
                reduce { SettingsUiState.Default(loginState) }
            }
        }

    // TODO - 로그아웃 추가 해야 함

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

    fun onSignOut() = intent {
        postSideEffect(SettingsSideEffect.NavigateToSignIn)
    }

    fun onDeleteAccount() = intent {
        postSideEffect(SettingsSideEffect.NavigateToDeleteAccount)
    }
}

sealed interface SettingsUiState{
    data class Default(
        val isLogin: Boolean = false
    ) : SettingsUiState
}

sealed interface SettingsSideEffect {
    data object NavigateToProfile : SettingsSideEffect
    data object OpenPlayStore : SettingsSideEffect
    data object OpenTermOfUse : SettingsSideEffect
    data object OpenPrivatePolicy : SettingsSideEffect
    data object NavigateToOnboarding : SettingsSideEffect
    data object NavigateToSignIn : SettingsSideEffect
    data object NavigateToDeleteAccount : SettingsSideEffect
}