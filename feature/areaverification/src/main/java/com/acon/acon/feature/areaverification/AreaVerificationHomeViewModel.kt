package com.acon.acon.feature.areaverification

import com.acon.acon.core.utils.feature.base.BaseContainerHost
import org.orbitmvi.orbit.viewmodel.container

class AreaVerificationHomeViewModel(

) : BaseContainerHost<AreaVerificationHomeUiState, AreaVerificationHomeSideEffect>() {

    override val container = container<AreaVerificationHomeUiState, AreaVerificationHomeSideEffect>(
        AreaVerificationHomeUiState.Success
    )

    fun onPermissionSettingClick(packageName: String) = intent {
        postSideEffect(AreaVerificationHomeSideEffect.NavigateToAppLocationSettings(packageName))
//        reduce {
//            state.copy(showPermissionDialog = false)
//        }
    }

    fun onGPSSettingClick(packageName: String) = intent {
        postSideEffect(AreaVerificationHomeSideEffect.NavigateToSystemLocationSettings(packageName))
//        reduce {
//            state.copy(showGPSDialog = false)
//        }
    }
}

sealed interface AreaVerificationHomeUiState {
    data object Success : AreaVerificationHomeUiState
}

sealed interface AreaVerificationHomeSideEffect {
    data object NavigateToBack : AreaVerificationHomeSideEffect

    data class NavigateToAppLocationSettings(
        val packageName: String
    ) : AreaVerificationHomeSideEffect

    data class NavigateToSystemLocationSettings(
        val packageName: String
    ) : AreaVerificationHomeSideEffect

    data class NavigateToNextScreen(
        val latitude: Double,
        val longitude: Double
    ) : AreaVerificationHomeSideEffect

}