package com.acon.feature.onboarding.area.viewmodel

import com.acon.acon.core.model.model.OnboardingPreferences
import com.acon.acon.core.ui.base.BaseContainerHost
import com.acon.acon.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VerifyInMapViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : BaseContainerHost<VerifyInMapState, VerifyInMapSideEffect>() {

    override val container = container<VerifyInMapState, VerifyInMapSideEffect>(VerifyInMapState()) {
        val currentLocation = getCurrentLocation()
        val verifyingLatitude = currentLocation.latitude
        val verifyingLongitude = currentLocation.longitude

        reduce {
            state.copy(
                latitude = verifyingLatitude,
                longitude = verifyingLongitude
            )
        }
    }

    fun onCompleteButtonClicked() = intent {
        onboardingRepository.verifyArea(
            latitude = state.latitude ?: error("latitude is null"),
            longitude = state.longitude ?: error("longitude is null")
        ).onSuccess {
            postSideEffect(VerifyInMapSideEffect.NavigateToNextScreen)
        }.onFailure { e ->
            Timber.e(e)
        }
    }

    fun onBackIconClicked() = intent {
        postSideEffect(VerifyInMapSideEffect.NavigateBack)
    }
}

data class VerifyInMapState(
    val latitude: Double? = null,
    val longitude: Double? = null
)

sealed interface VerifyInMapSideEffect {
    data object NavigateToNextScreen : VerifyInMapSideEffect
    data object NavigateBack : VerifyInMapSideEffect
}