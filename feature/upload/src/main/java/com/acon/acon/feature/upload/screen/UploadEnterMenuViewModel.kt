package com.acon.acon.feature.upload.screen

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.acon.acon.core.model.model.spot.SimpleSpot
import com.acon.acon.core.navigation.route.UploadRoute
import com.acon.acon.core.navigation.type.simpleSpotNavType
import com.acon.acon.core.ui.base.BaseContainerHost
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container

@OptIn(FlowPreview::class)
class UploadEnterMenuViewModel(
    savedStateHandle: SavedStateHandle
) : BaseContainerHost<UploadEnterMenuUiState, UploadEnterMenuSideEffect>() {

    private val typeMap = mapOf(simpleSpotNavType)
    private val spot = savedStateHandle.toRoute<UploadRoute.EnterMenu>(typeMap).spot

    private val queryFlow = MutableStateFlow("")

    override val container =
        container<UploadEnterMenuUiState, UploadEnterMenuSideEffect>(UploadEnterMenuUiState(recommendMenu = spot.recommendedMenu ?: "")) {
        viewModelScope.launch {
            queryFlow
                .debounce(100)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isBlank()) {
                        reduce { state.copy(recommendMenu = "") }
                    } else {
                        reduce { state.copy(recommendMenu = query) }
                    }
                }
        }
    }

    fun onNextAction() = intent {
        postSideEffect(
            UploadEnterMenuSideEffect.NavigateToReview(
                spot = spot.copy(
                    recommendedMenu = state.recommendMenu
                )
            )
        )
    }


    fun onBackAction() = intent {
        postSideEffect(UploadEnterMenuSideEffect.NavigateToBack)
    }


    fun onSearchQueryChanged(query: String) = intent {
        queryFlow.value = query
    }
}

@Immutable
data class UploadEnterMenuUiState(
    val recommendMenu: String = ""
)

sealed interface UploadEnterMenuSideEffect {
    data object NavigateToBack : UploadEnterMenuSideEffect
    data class NavigateToReview(val spot: SimpleSpot) : UploadEnterMenuSideEffect
}