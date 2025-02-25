package com.acon.acon.feature.areaverification

import android.app.Application
import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.repository.AreaVerificationRepository
import com.acon.acon.domain.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AreaVerificationViewModel @Inject constructor(
    private val application: Application,
    private val tokenRepository: TokenRepository,
    private val areaVerificationRepository: AreaVerificationRepository
) : BaseContainerHost<AreaVerificationState, AreaVerificationSideEffect>() {

    override val container = container<AreaVerificationState, AreaVerificationSideEffect>(
        AreaVerificationState(
            isNewLocationSelected = true,
        )
    ) {
        fetchVerifiedArea()
        checkGPSStatus()
        checkSupportLocation(state.latitude, state.longitude)
    }

    fun checkGPSStatus() = intent {
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        Log.d("로그", "isGPSEnabled : $isGPSEnabled")

        if (isGPSEnabled) {
            reduce { state.copy(isGPSEnabled = true, showGPSDialog = false)}
        } else {
            reduce { state.copy(isGPSEnabled = false, showGPSDialog = true) }
        }
    }

    fun hideGPSDialog() = intent {
        reduce {
            state.copy(showGPSDialog = false)
        }
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

    fun onGPSSettingClick(packageName: String) = intent {
        postSideEffect(AreaVerificationSideEffect.NavigateToGPSSettings(packageName))
        reduce {
            state.copy(showGPSDialog = false)
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

     fun fetchVerifiedArea() = intent {
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

    fun checkSupportLocation(latitude: Double, longitude: Double) = intent {
        val isSupportLocation = latitude in 33.1..38.6 && longitude in 124.6..131.9
        reduce {
            state.copy(isSupportLocation = isSupportLocation)
        }
    }

    fun showLocationDialog() = intent {
        reduce {
            state.copy(showLocationDialog = true)
        }
    }

    fun hideLocationDialog() = intent {
        reduce {
            state.copy(showLocationDialog = false)
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
                tokenRepository.saveAreaVerification(true)
                reduce {
                    state.copy(
                        isLoading = false,
                        verifiedArea = area
                    )
                }
            }
            .onFailure { throwable ->
                tokenRepository.saveAreaVerification(false)
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
