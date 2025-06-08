package com.acon.acon.feature.profile.composable.screen.bookmark

import com.acon.acon.domain.repository.ProfileRepository
import com.acon.feature.common.base.BaseContainerHost
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : BaseContainerHost<BookmarkUiState, BookmarkUiSideEffect>() {

    override val container =  container<BookmarkUiState, BookmarkUiSideEffect>(BookmarkUiState.Loading) {
        // TODO
    }

    fun navigateToBack() = intent {
        postSideEffect(BookmarkUiSideEffect.OnNavigateToBack)
    }

    // TODO - 정보 담아서 보내야 함
    fun onSpotClicked() = intent {
        postSideEffect(BookmarkUiSideEffect.OnNavigateToSpotDetailScreen)
    }
}

sealed interface BookmarkUiState {
    data object Success : BookmarkUiState
    data object Loading : BookmarkUiState
    data object LoadFailed : BookmarkUiState
}

sealed interface BookmarkUiSideEffect {
    data object OnNavigateToSpotDetailScreen : BookmarkUiSideEffect
    data object OnNavigateToBack : BookmarkUiSideEffect
}