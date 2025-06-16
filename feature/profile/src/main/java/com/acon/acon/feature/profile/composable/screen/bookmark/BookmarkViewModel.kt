package com.acon.acon.feature.profile.composable.screen.bookmark

import com.acon.acon.domain.model.profile.SavedSpot
import com.acon.acon.domain.repository.SpotRepository
import com.acon.feature.common.base.BaseContainerHost
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val spotRepository: SpotRepository
) : BaseContainerHost<BookmarkUiState, BookmarkUiSideEffect>() {

    override val container =  container<BookmarkUiState, BookmarkUiSideEffect>(BookmarkUiState.Loading) {
        fetchSavedSpotList()
    }

    private fun fetchSavedSpotList() = intent {
        spotRepository.fetchSavedSpotList().onSuccess {
            reduce {
                BookmarkUiState.Success(savedSpots = it)
            }
        }.onFailure {
            reduce {
                BookmarkUiState.LoadFailed
            }
        }
    }


    fun navigateToBack() = intent {
        postSideEffect(BookmarkUiSideEffect.OnNavigateToBack)
    }

    fun onSpotClicked(spotId: Long) = intent {
        runOn<BookmarkUiState.Success> {
            postSideEffect(BookmarkUiSideEffect.OnNavigateToSpotDetailScreen(spotId))
        }
    }
}

sealed interface BookmarkUiState {
    data class Success(val savedSpots: List<SavedSpot>? = emptyList()) : BookmarkUiState
    data object Loading : BookmarkUiState
    data object LoadFailed : BookmarkUiState
}

sealed interface BookmarkUiSideEffect {
    data class OnNavigateToSpotDetailScreen(val spotId: Long) : BookmarkUiSideEffect
    data object OnNavigateToBack : BookmarkUiSideEffect
}