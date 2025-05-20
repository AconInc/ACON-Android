package com.acon.acon.feature.upload.screen

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.model.upload.v2.SearchedSpot
import com.acon.acon.feature.upload.UploadRoute
import com.acon.feature.common.navigation.searchedSpotNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class UploadReviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseContainerHost<UploadReviewUiState, UploadReviewSideEffect>() {

    private val typeMap = mapOf(searchedSpotNavType)

    private val searchedSpot = savedStateHandle.toRoute<UploadRoute.Review>(typeMap).searchedSpot

    override val container =
        container<UploadReviewUiState, UploadReviewSideEffect>(UploadReviewUiState.Success(spot = searchedSpot)) {

        }

    fun onBackAction() = intent {
        postSideEffect(UploadReviewSideEffect.NavigateBack)
    }

    fun onCompletion() = intent {
        runOn<UploadReviewUiState.Success> {
            // TODO : 업로드 API 호출
            postSideEffect(UploadReviewSideEffect.NavigateToComplete(state.spot.name))
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
        val spot: SearchedSpot,
        val selectedDotoriCount: Int = 0,
    ): UploadReviewUiState
    data object LoadFailed: UploadReviewSideEffect
}

sealed interface UploadReviewSideEffect {
    data object NavigateBack : UploadReviewSideEffect
    data class NavigateToComplete(val spotName: String) : UploadReviewSideEffect
}