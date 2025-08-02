package com.acon.acon.feature.areaverification.composable

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.acon.acon.core.model.type.UserActionType
import com.acon.acon.core.ui.base.BaseContainerHost
import com.acon.acon.domain.error.area.ReplaceVerifiedArea
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.acon.domain.repository.TimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AreaVerificationViewModel @Inject constructor(
    private val application: Application,
    private val profileRepository: ProfileRepository,
    private val timeRepository: TimeRepository
) : BaseContainerHost<AreaVerificationUiState, AreaVerificationSideEffect>() {

    override val container = container<AreaVerificationUiState, AreaVerificationSideEffect>(
        AreaVerificationUiState()
    ) {
        checkDeviceGPSStatus()
    }

    fun checkDeviceGPSStatus() = intent {
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
    }

    fun onSkipButtonClick() = intent {
        postSideEffect(AreaVerificationSideEffect.NavigateToOnboarding)
        timeRepository.saveUserActionTime(UserActionType.SKIP_AREA_VERIFICATION, System.currentTimeMillis())
    }

    fun onDeviceGPSSettingClick(packageName: String) = intent {
        postSideEffect(
            AreaVerificationSideEffect.NavigateToSystemLocationSettings(packageName)
        )
        reduce {
            state.copy(showDeviceGPSDialog = false)
        }
    }

    fun editVerifiedArea(previousVerifiedAreaId: Long, latitude: Double, longitude: Double) = intent {
        profileRepository.replaceVerifiedArea(
            previousVerifiedAreaId = previousVerifiedAreaId,
            latitude = latitude,
            longitude = longitude
        ).onSuccess {
            reduce {
                state.copy(
                    isVerifySuccess = true
                )
            }
        }.onFailure { error ->
            when(error) {
                is ReplaceVerifiedArea.OutOfServiceAreaError -> {
                    postSideEffect(AreaVerificationSideEffect.ShowErrorToast("서비스를 제공하지 않는 지역입니다."))
                }
                is ReplaceVerifiedArea.InvalidVerifiedArea -> {
                    postSideEffect(AreaVerificationSideEffect.ShowErrorToast("유효하지 않은 인증 지역입니다."))
                }
                is ReplaceVerifiedArea.VerifiedAreaNotFound -> {
                    postSideEffect(AreaVerificationSideEffect.ShowErrorToast("존재하지 않는 인증 지역입니다."))
                }
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
            }
    }

    fun verifyArea(latitude: Double, longitude: Double) = intent {
        profileRepository.verifyArea(latitude, longitude)
            .onSuccess {
                reduce {
                    state.copy(
                        isVerifySuccess = true
                    )
                }
            }
            .onFailure {
                postSideEffect(AreaVerificationSideEffect.ShowErrorToast("지역인증에 실패했습니다. 다시 시도해주세요."))
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
    val verifiedAreaList: List<com.acon.acon.core.model.model.area.Area> = emptyList(),
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

    data class ShowErrorToast(val errorMessage: String) : AreaVerificationSideEffect

    data object NavigateToOnboarding : AreaVerificationSideEffect
}