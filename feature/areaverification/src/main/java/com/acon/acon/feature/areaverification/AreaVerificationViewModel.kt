package com.acon.acon.feature.areaverification

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.repository.AreaVerificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AreaVerificationViewModel @Inject constructor(
    private val areaVerificationRepository: AreaVerificationRepository
) : BaseContainerHost<AreaVerificationState, AreaVerificationSideEffect>() {

    override val container = container<AreaVerificationState, AreaVerificationSideEffect>(
        AreaVerificationState(
            isNewLocationSelected = true,
        )
    ) {
        fetchVerifiedArea()
    }

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

    fun updateLocationPermissionStatus(isGranted: Boolean) = intent {
        reduce {
            state.copy(hasLocationPermission = isGranted)
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

     private fun fetchVerifiedArea() = intent {
        viewModelScope.launch {
            areaVerificationRepository.fetchVerifiedAreaList()
                .onSuccess { verifiedAreaList ->
                    reduce {
                        state.copy(verifiedAreaList = verifiedAreaList)
                    }
                }
                .onFailure {

                }
        }
    }

    fun editVerifiedArea(verifiedAreaId: Long, latitude: Double, longitude: Double) = intent {
        reduce {
            state.copy(
                isLoading = true,
                error = null
            )
        }

        areaVerificationRepository.verifyArea(latitude, longitude)
            .onSuccess { newVerifiedArea->
                if(state.verifiedAreaList[0].verifiedAreaId != newVerifiedArea.verifiedAreaId) {
                    areaVerificationRepository.deleteVerifiedArea(verifiedAreaId)
                        .onSuccess {
                            reduce { state.copy(isLoading = false, verifiedArea = newVerifiedArea) }
                        }
                        .onFailure { deleteError ->
                            reduce { state.copy(isLoading = false, error = deleteError.message) }
                        }
                } else {
                    reduce {
                        state.copy(
                            isLoading = false,
                            verifiedArea = newVerifiedArea
                        )
                    }
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
