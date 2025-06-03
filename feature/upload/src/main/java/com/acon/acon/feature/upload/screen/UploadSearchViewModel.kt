package com.acon.acon.feature.upload.screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Immutable
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.model.spot.SimpleSpot
import com.acon.acon.domain.model.upload.UploadSpotSuggestion
import com.acon.acon.domain.model.upload.v2.SearchedSpot
import com.acon.acon.domain.repository.UploadRepository
import com.acon.feature.common.location.getLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class, FlowPreview::class)
@HiltViewModel
class UploadSearchViewModel @Inject constructor(
    private val uploadRepository: UploadRepository,
    @ApplicationContext private val context: Context,
) : BaseContainerHost<UploadSearchUiState, UploadSearchSideEffect>() {

    @SuppressLint("MissingPermission")
    override val container = container<UploadSearchUiState, UploadSearchSideEffect>(UploadSearchUiState.Success()) {
        val currentLocation = context.getLocation()
        currentLocation?.let {
            uploadRepository.getSuggestions(it.latitude, it.longitude).onSuccess {
                runOn<UploadSearchUiState.Success> {
                    reduce {
                        state.copy(
                            uploadSpotSuggestions = it
                        )
                    }
                }
            }.onFailure {

            }
        }
        queryFlow
            .debounce(300)
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .collect { query ->
                uploadRepository.getSearchedSpots(query).onSuccess {
                        runOn<UploadSearchUiState.Success> {
                            reduce {
                                state.copy(
                                    searchedSpots = it
                                )
                            }
                        }
                    }.onFailure {  }
            }
    }

    private val queryFlow = MutableStateFlow("")

    fun onSearchQueryChanged(query: String) = intent {
        runOn<UploadSearchUiState.Success> {
            reduce {
                state.copy(
                    query = query,
                    selectedSpot = null
                )
            }
            queryFlow.value = query
        }
    }

    fun onSuggestionSpotClicked(spot: UploadSpotSuggestion) = intent {
        runOn<UploadSearchUiState.Success> {
            reduce {
                state.copy(
                    query = spot.name,
                    selectedSpot = SimpleSpot(spot.spotId.toInt(), spot.name)
                )
            }
        }
    }

    fun onSearchedSpotClicked(spot: SearchedSpot) = intent {
        runOn<UploadSearchUiState.Success> {
            reduce {
                state.copy(
                    query = spot.name,
                    selectedSpot = SimpleSpot(spot.spotId.toInt(), spot.name)
                )
            }
        }
    }

    fun onBackAction() = intent {
        postSideEffect(UploadSearchSideEffect.NavigateBack)
    }

    fun onNextAction() = intent {
        runOn<UploadSearchUiState.Success> {
            state.selectedSpot?.let {
                postSideEffect(UploadSearchSideEffect.NavigateToReviewScreen(it))
            }
        }
    }
}

sealed interface UploadSearchUiState {
    @Immutable
    data class Success(
        val uploadSpotSuggestions: List<UploadSpotSuggestion> = listOf(),
        val query: String = "",
        val selectedSpot: SimpleSpot? = null,
        val searchedSpots: List<SearchedSpot> = listOf(),
    ) : UploadSearchUiState
}

sealed interface UploadSearchSideEffect {
    data class NavigateToReviewScreen(val spot: SimpleSpot) : UploadSearchSideEffect
    data object NavigateBack : UploadSearchSideEffect
}