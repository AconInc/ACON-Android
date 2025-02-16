package com.acon.acon.feature.areaverification

import androidx.lifecycle.ViewModel
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.repository.AreaVerificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AreaVerificationViewModel @Inject constructor(
    private val areaVerificationRepository: AreaVerificationRepository
) : BaseContainerHost<AreaVerificationState, AreaVerificationSideEffect>() {

    override val container = container<AreaVerificationState, AreaVerificationSideEffect>(
        AreaVerificationState(isNewLocationSelected = true)
    )

    fun onNewLocationSelected() = intent {
        reduce {
            state.copy(isNewLocationSelected = !state.isNewLocationSelected)
        }
    }

    fun updateShowPermissionDialog(show: Boolean) = intent {
        reduce {
            state.copy(showPermissionDialog = show)
        }
    }

    fun onNextButtonClick() = intent {
        if (state.isNewLocationSelected) {
            postSideEffect(
                AreaVerificationSideEffect.NavigateToNextScreen(
                    state.latitude,
                    state.longitude
                )
            )
        }
    }

    fun onPermissionSettingClick(packageName: String) = intent {
        postSideEffect(AreaVerificationSideEffect.NavigateToSettings(packageName))
        reduce {
            state.copy(showPermissionDialog = false)
        }
    }

    fun checkLocationAndNavigate() = intent {
        if (state.isLocationObtained) {
            postSideEffect(
                AreaVerificationSideEffect.NavigateToNewArea(
                    state.latitude,
                    state.longitude
                )
            )
        }
    }

    fun verifyArea(latitude: Double, longitude: Double) = intent {
        reduce {
            state.copy(
                isLoading = true,
                error = null
            )
        }

        areaVerificationRepository.verifyArea(latitude, longitude)
            .onSuccess { area ->
                reduce {
                    state.copy(
                        isLoading = false,
                        verifiedArea = area
                    )
                }
            }
            .onFailure { throwable ->
                reduce {
                    state.copy(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            }
    }

    fun resetVerifiedArea() = intent {
        reduce {
            state.copy(verifiedArea = null)
        }
    }
}
