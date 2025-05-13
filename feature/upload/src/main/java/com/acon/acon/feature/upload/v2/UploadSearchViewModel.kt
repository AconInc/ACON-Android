package com.acon.acon.feature.upload.v2

import androidx.compose.runtime.Immutable
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.model.upload.v2.SearchedSpot
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class UploadSearchViewModel @Inject constructor(

) : BaseContainerHost<UploadSearchUiState, UploadSearchSideEffect>() {
    override val container = container<UploadSearchUiState, UploadSearchSideEffect>(UploadSearchUiState.Success()) {
        // Init state
    }

    fun onSearchQueryChanged(query: String) = intent {
        runOn<UploadSearchUiState.Success> {
            reduce {
                state.copy(
                    query = query
                )
            }
        }
    }

    fun onBackAction() = intent {
        postSideEffect(UploadSearchSideEffect.NavigateBack)
    }

    fun onNextAction() = intent {
        postSideEffect(UploadSearchSideEffect.NavigateToReviewScreen)
    }
}

sealed interface UploadSearchUiState {
    @Immutable
    data class Success(
        val suggestions: List<String> = listOf(),
        val query: String = "",
        val searchedSpots: List<SearchedSpot> = listOf(),
    ) : UploadSearchUiState
    data object LoadFailed : UploadSearchUiState
}

sealed interface UploadSearchSideEffect {
    data object NavigateToReviewScreen : UploadSearchSideEffect
    data object NavigateBack : UploadSearchSideEffect
}