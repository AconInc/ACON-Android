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
            .debounce(200)
            .distinctUntilChanged()
            .collect { query ->
                runOn<UploadSearchUiState.Success> {
                    if (query.isBlank()) {
                        reduce {
                            state.copy(
                                searchedSpots = emptyList(),
                                showSearchedSpots = false
                            )
                        }
                    } else {
                        uploadRepository.getSearchedSpots(query).onSuccess {
                            reduce {
                                state.copy(
                                    searchedSpots = it,
                                    showSearchedSpots = it.isNotEmpty()
                                )
                            }

                        }.onFailure { }
                    }
                }
            }
    }

    private val queryFlow = MutableStateFlow("")

    fun onSearchQueryChanged(query: String) = intent {
        runOn<UploadSearchUiState.Success> {
            reduce {
                state.copy(
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
                    selectedSpot = SimpleSpot(spot.spotId.toInt(), spot.name),
                    showSearchedSpots = false
                )
            }
        }
    }

    fun onSearchedSpotClicked(spot: SearchedSpot) = intent {
        runOn<UploadSearchUiState.Success> {
            reduce {
                state.copy(
                    selectedSpot = SimpleSpot(spot.spotId.toInt(), spot.name),
                    showSearchedSpots = false
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
        val selectedSpot: SimpleSpot? = null,
        val searchedSpots: List<SearchedSpot> = listOf(),
        val showSearchedSpots: Boolean = false
    ) : UploadSearchUiState
}

sealed interface UploadSearchSideEffect {
    data class NavigateToReviewScreen(val spot: SimpleSpot) : UploadSearchSideEffect
    data object NavigateBack : UploadSearchSideEffect
}