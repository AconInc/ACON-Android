package com.acon.acon.feature.areaverification.v2

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.model.area.Area
import com.acon.acon.domain.repository.AreaVerificationRepository
import com.acon.acon.domain.repository.TokenRepository
import com.acon.acon.feature.areaverification.amplitudeClickNext
import com.acon.acon.feature.areaverification.amplitudeCompleteArea
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AreaVerificationHomeViewModel @Inject constructor(
    private val application: Application,
    private val tokenRepository: TokenRepository,
    private val areaVerificationRepository: AreaVerificationRepository
) : BaseContainerHost<AreaVerificationHomeUiState, AreaVerificationHomeSideEffect>() {

    override val container = container<AreaVerificationHomeUiState, AreaVerificationHomeSideEffect>(
        AreaVerificationHomeUiState(
            isNewLocationSelected = true,
        )
    ) {
        fetchVerifiedArea()
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

    fun showPermissionDialog() = intent {
        reduce {
            state.copy(showPermissionDialog = true)
        }
    }

    fun showLocationDialog() = intent {
        reduce {
            state.copy(showLocationDialog = true)
        }
    }

    // TODO - api 연결 때 사용 또는 제거
    fun hideLocationDialog() = intent {
        reduce {
            state.copy(showLocationDialog = false)
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
                AreaVerificationHomeSideEffect.NavigateToNextScreen(
                    state.latitude,
                    state.longitude
                )
            )
        }
        amplitudeClickNext()
    }

    fun onPermissionSettingClick(packageName: String) = intent {
        postSideEffect(
            AreaVerificationHomeSideEffect.NavigateToAppLocationSettings(packageName)
        )
        reduce {
            state.copy(showPermissionDialog = false)
        }
    }

    fun onDeviceGPSSettingClick(packageName: String) = intent {
        postSideEffect(
            AreaVerificationHomeSideEffect.NavigateToSystemLocationSettings(packageName)
        )
        reduce {
            state.copy(showDeviceGPSDialog = false)
        }
    }

    fun fetchVerifiedArea() = intent {
        areaVerificationRepository.fetchVerifiedAreaList()
            .onSuccess { verifiedAreaList ->
                reduce {
                    state.copy(verifiedAreaList = verifiedAreaList)
                }
            }
            .onFailure {

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
            .onSuccess { newVerifiedArea ->
                if (state.verifiedAreaList[0].verifiedAreaId != newVerifiedArea.verifiedAreaId) {
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
                amplitudeCompleteArea()
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

    fun checkSupportLocation(context: Context) = intent {

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@intent
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        var isSupportLocation: Boolean

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude

                isSupportLocation = latitude in 33.1..38.6 && longitude in 124.6..131.9
                if (!isSupportLocation) {
                    Timber.tag(TAG).d("GPS 불가 지역(해외)")
                    showLocationDialog()
                }
            } else {
                Timber.tag(TAG).d("GPS 좌표 가져오기 실패")
            }

        }.addOnFailureListener { e ->
            Timber.tag(TAG).d("GPS 좌표 가져오기 오류 발생, ${e.message}")
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
                        verifiedArea = area,
                        areaName = area.name
                    )
                }
                amplitudeCompleteArea()
            }
            .onFailure { throwable ->
                tokenRepository.saveAreaVerification(false)
                reduce {
                    state.copy(
                        isLoading = false,
                        error = throwable.message,
                    )
                }
            }
    }

    companion object {
        const val TAG = "AreaVerificationViewModel"
    }
}

data class AreaVerificationHomeUiState(
    val isNewLocationSelected: Boolean = false,
    val isButtonEnabled: Boolean = true,
    val isLocationPermissionGranted: Boolean = false,
    var showPermissionDialog: Boolean = false,
    val hasLocationPermission: Boolean = false,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isLocationObtained: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val areaName: String = "",
    val verifiedArea: Area? = null,
    val verifiedAreaList: List<Area> = emptyList(),
    val isGPSEnabled: Boolean = false,
    val showDeviceGPSDialog: Boolean = false,
    val showLocationDialog: Boolean = false,
)

sealed interface AreaVerificationHomeSideEffect {

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