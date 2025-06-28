package com.acon.acon.feature.upload.screen

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.acon.acon.core.ui.base.BaseContainerHost
import com.acon.acon.core.model.model.spot.SimpleSpot
import com.acon.acon.domain.repository.UploadRepository
import com.acon.acon.core.navigation.route.UploadRoute
import com.acon.core.analytics.amplitude.AconAmplitude
import com.acon.core.analytics.constants.EventNames
import com.acon.core.analytics.constants.PropertyKeys
import com.acon.acon.core.navigation.type.simpleSpotNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class UploadReviewViewModel @Inject constructor(
    private val uploadRepository: UploadRepository,
    savedStateHandle: SavedStateHandle
) : BaseContainerHost<UploadReviewUiState, UploadReviewSideEffect>() {

    private val typeMap = mapOf(simpleSpotNavType)
    private val spot = savedStateHandle.toRoute<UploadRoute.Review>(typeMap).spot

    override val container =
        container<UploadReviewUiState, UploadReviewSideEffect>(UploadReviewUiState.Success(spot = spot)) {

        }

    fun onBackAction() = intent {
        postSideEffect(UploadReviewSideEffect.NavigateBack)
    }

    fun onCompletion() = intent {
        runOn<UploadReviewUiState.Success> {
            if (state.selectedAcornCount < 1) {
                postSideEffect(UploadReviewSideEffect.ShowToast)
                return@runOn
            }
            AconAmplitude.trackEvent(
                eventName = EventNames.UPLOAD,
                property = PropertyKeys.SPOT_ID to spot.spotId,
            )
            AconAmplitude.trackEvent(
                eventName = EventNames.UPLOAD,
                property = PropertyKeys.CLICK_REVIEW_ACON to true
            )

            uploadRepository.submitReview(
                spotId = spot.spotId,
                acornCount = state.selectedAcornCount
            ).onSuccess {
                postSideEffect(UploadReviewSideEffect.NavigateToComplete(state.spot))
            }.onFailure {
                postSideEffect(UploadReviewSideEffect.ShowToast)
            }
        }
    }

    fun onAcornCountChanged(count: Int) = intent {
        runOn<UploadReviewUiState.Success> {
            reduce {
                state.copy(
                    selectedAcornCount = count
                )
            }
        }
    }
}

sealed interface UploadReviewUiState {
    data class Success(
        val spot: com.acon.acon.core.model.model.spot.SimpleSpot,
        val selectedAcornCount: Int = 0,
    ): UploadReviewUiState
    data object LoadFailed: UploadReviewSideEffect
}

sealed interface UploadReviewSideEffect {
    data object NavigateBack : UploadReviewSideEffect
    data object ShowToast : UploadReviewSideEffect
    data class NavigateToComplete(val spot: com.acon.acon.core.model.model.spot.SimpleSpot) : UploadReviewSideEffect
}