package com.acon.acon.feature.areaverification.composable

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.acon.feature.common.base.BaseContainerHost
import com.acon.acon.domain.model.area.Area
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.feature.areaverification.amplitude.amplitudeClickNext
import com.acon.acon.feature.areaverification.amplitude.amplitudeCompleteArea
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AreaVerificationViewModel @Inject constructor(
    private val application: Application,
    private val userRepository: UserRepository,
) : BaseContainerHost<AreaVerificationUiState, AreaVerificationSideEffect>() {

    override val container = container<AreaVerificationUiState, AreaVerificationSideEffect>(
        AreaVerificationUiState()
    ) {
        checkDeviceGPSStatus()
    }

    fun checkDeviceGPSStatus() = intent {
        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (isGPSEnabled) {
            reduce { state.copy(isGPSEnabled = true, showDeviceGPSDialog = false) }
        } else {
            reduce { state.copy(isGPSEnabled = false, showDeviceGPSDialog = true) }
        }
    }

    private fun showLocationDialog() = intent {
        reduce {
            state.copy(showLocationDialog = true)
        }
    }

    fun onNextButtonClick() = intent {
        postSideEffect(
            AreaVerificationSideEffect.NavigateToNextScreen(
                state.latitude,
                state.longitude
            )
        )
        amplitudeClickNext()
    }

    fun onDeviceGPSSettingClick(packageName: String) = intent {
        postSideEffect(
            AreaVerificationSideEffect.NavigateToSystemLocationSettings(packageName)
        )
        reduce {
            state.copy(showDeviceGPSDialog = false)
        }
    }

    fun editVerifiedArea(area: String, latitude: Double, longitude: Double) = intent {
        reduce {
            state.copy(
                error = null
            )
        }

        val verifiedAreaList = userRepository.fetchVerifiedAreaList().getOrElse { emptyList() }
        val verifiedAreaId = verifiedAreaList[0].verifiedAreaId

        if(verifiedAreaList[0].name == area) {
            reduce {
                state.copy(
                    isVerifySuccess = true
                )
            }
        } else {
            userRepository.verifyArea(latitude, longitude)
                .onSuccess { newVerifiedArea ->
                    if (verifiedAreaId != newVerifiedArea.verifiedAreaId) {
                        userRepository.deleteVerifiedArea(verifiedAreaId)
                            .onSuccess {
                                reduce {
                                    state.copy(
                                        verifiedArea = newVerifiedArea,
                                        isVerifySuccess = true
                                    )
                                }
                            }
                            .onFailure { deleteError ->
                                reduce { state.copy(error = deleteError.message) }
                                // TODO - 네트워크에러
                            }
                    } else {
                        reduce {
                            state.copy(
                                verifiedArea = newVerifiedArea,
                                isVerifySuccess = true
                            )
                        }
                    }
                    amplitudeCompleteArea()
                }
                .onFailure { throwable ->
                    reduce {
                        state.copy(
                            error = throwable.message
                        )
                    }
                    // TODO - 네트워크에러
                }
        }
    }

    fun checkSupportLocation(context: Context) = intent {

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@intent
        }

        var isSupportLocation: Boolean

        getCurrentLocation().let { location ->
                val latitude = location.latitude
                val longitude = location.longitude
                isSupportLocation = latitude in 33.1..38.6 && longitude in 124.6..131.9

                if (!isSupportLocation) {
                    Timber.tag(TAG).d("GPS 불가 지역(해외)")
                    showLocationDialog()
                }
            } ?: run {
            Timber.tag(TAG).d("GPS 좌표 가져오기 실패")
        }
    }

    fun verifyArea(latitude: Double, longitude: Double) = intent {
        reduce {
            state.copy(
                error = null
            )
        }

        userRepository.verifyArea(latitude, longitude)
            .onSuccess { area ->
                reduce {
                    state.copy(
                        verifiedArea = area,
                        areaName = area.name,
                        isVerifySuccess = true
                    )
                }
                amplitudeCompleteArea()
            }
            .onFailure { throwable ->
                reduce {
                    state.copy(
                        error = throwable.message,
                    )
                }
                // TODO - 네트워크에러
            }
    }

    companion object {
        const val TAG = "AreaVerificationViewModel"
    }
}

data class AreaVerificationUiState(
    val isGPSEnabled: Boolean = false,
    val showDeviceGPSDialog: Boolean = false,
    val showLocationDialog: Boolean = false,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isVerifySuccess: Boolean = false,
    val verifiedArea: Area? = null,
    val verifiedAreaList: List<Area> = emptyList(),
    val areaName: String = "",
    val error: String? = null
)

sealed interface AreaVerificationSideEffect {

    data class NavigateToAppLocationSettings(
        val packageName: String
    ) : AreaVerificationSideEffect

    data class NavigateToSystemLocationSettings(
        val packageName: String
    ) : AreaVerificationSideEffect

    data class NavigateToNextScreen(
        val latitude: Double,
        val longitude: Double
    ) : AreaVerificationSideEffect
}