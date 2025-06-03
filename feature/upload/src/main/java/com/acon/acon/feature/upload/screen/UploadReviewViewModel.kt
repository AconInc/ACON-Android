package com.acon.acon.feature.upload.screen

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.model.spot.SimpleSpot
import com.acon.acon.domain.repository.UploadRepository
import com.acon.acon.feature.upload.UploadRoute
import com.acon.feature.common.navigation.simpleSpotNavType
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
            if (state.selectedDotoriCount < 1) {
                postSideEffect(UploadReviewSideEffect.ShowToast)
                return@runOn
            }

            uploadRepository.submitReview(
                spotId = spot.spotId,
                acornCount = state.selectedDotoriCount
            ).onSuccess {
                postSideEffect(UploadReviewSideEffect.NavigateToComplete(state.spot))
            }.onFailure {
                postSideEffect(UploadReviewSideEffect.ShowToast)
            }
        }
    }

    fun onDotoriCountChanged(count: Int) = intent {
        runOn<UploadReviewUiState.Success> {
            reduce {
                state.copy(
                    selectedDotoriCount = count
                )
            }
        }
    }
}

sealed interface UploadReviewUiState {
    data class Success(
        val spot: SimpleSpot,
        val selectedDotoriCount: Int = 0,
    ): UploadReviewUiState
    data object LoadFailed: UploadReviewSideEffect
}

sealed interface UploadReviewSideEffect {
    data object NavigateBack : UploadReviewSideEffect
    data object ShowToast : UploadReviewSideEffect
    data class NavigateToComplete(val spot: SimpleSpot) : UploadReviewSideEffect
}