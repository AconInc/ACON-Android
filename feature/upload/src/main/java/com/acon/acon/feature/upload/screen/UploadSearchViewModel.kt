package com.acon.acon.feature.upload.screen

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.acon.acon.domain.error.upload.GetVerifySpotLocationError
import com.acon.acon.domain.model.spot.SimpleSpot
import com.acon.acon.domain.model.upload.UploadSpotSuggestion
import com.acon.acon.domain.model.upload.SearchedSpot
import com.acon.acon.domain.repository.UploadRepository
import com.acon.acon.feature.upload.BuildConfig
import com.acon.acon.feature.upload.mock.uploadSearchUiStateMock
import com.acon.feature.common.base.BaseContainerHost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class, FlowPreview::class)
@HiltViewModel
class UploadSearchViewModel @Inject constructor(
    private val uploadRepository: UploadRepository,
) : BaseContainerHost<UploadSearchUiState, UploadSearchSideEffect>() {

    override val container = container<UploadSearchUiState, UploadSearchSideEffect>(UploadSearchUiState.Success()) {
        intent {
            getCurrentLocation().run {
                uploadRepository.getSuggestions(latitude, longitude).onSuccess {
                    runOn<UploadSearchUiState.Success> {
                        reduce {
                            state.copy(
                                uploadSpotSuggestions = it
                            )
                        }
                    }
                }.onFailure {
                    runOn<UploadSearchUiState.Success> {
                        if (BuildConfig.DEBUG)
                            reduce {
                                state.copy(
                                    uploadSpotSuggestions = uploadSearchUiStateMock.uploadSpotSuggestions
                                )
                            }
                    }
                }
            }
        }
        viewModelScope.launch {
            queryFlow
                .debounce(100)
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
                                        searchedSpots = it
                                    )
                                }

                            }.onFailure { }
                        }
                    }
                }
        }
    }

    private val queryFlow = MutableStateFlow("")

    fun onSearchQueryChanged(query: String, isSelection: Boolean) = intent {
        runOn<UploadSearchUiState.Success> {
            reduce {
                if (isSelection)
                    state.copy(
                        showSearchedSpots = false
                    )
                else
                    state.copy(
                        selectedSpot = null,
                        showSearchedSpots = query.isNotBlank()
                    )
            }
            queryFlow.value = query
        }
    }

    fun onSuggestionSpotClicked(spot: UploadSpotSuggestion, onSuccess: () -> Unit) = intent {
        runOn<UploadSearchUiState.Success> {
            val verifyingLocation = getCurrentLocation()
            uploadRepository.verifyLocation(
                spot.spotId,
                verifyingLocation.latitude,
                verifyingLocation.longitude
            ).onSuccess {
                onSuccess()
                reduce {
                    state.copy(
                        selectedSpot = SimpleSpot(spot.spotId, spot.name),
                        showSearchedSpots = false
                    )
                }
            }.onFailure { e ->
                reduce {
                    if (e is GetVerifySpotLocationError.OutOfServiceAreaError)
                        state.copy(
                            showNotInKoreaDialog = true
                        )
                    else
                        state.copy(
                            showNotAvailableLocationDialog = true
                        )
                }
            }
        }
    }

    fun onSearchedSpotClicked(spot: SearchedSpot, onSuccess: () -> Unit) = intent {
        runOn<UploadSearchUiState.Success> {
            onSuggestionSpotClicked(
                UploadSpotSuggestion(spot.spotId, spot.name), onSuccess
            )
        }
    }

    fun onVerifyLocationDialogAction() = intent {
        runOn<UploadSearchUiState.Success> {
            reduce {
                state.copy(
                    showNotAvailableLocationDialog = false,
                    showNotInKoreaDialog = false
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
        val showSearchedSpots: Boolean = false,
        val showNotAvailableLocationDialog: Boolean = false,
        val showNotInKoreaDialog: Boolean = false
    ) : UploadSearchUiState
}

sealed interface UploadSearchSideEffect {
    data class NavigateToReviewScreen(val spot: SimpleSpot) : UploadSearchSideEffect
    data object NavigateBack : UploadSearchSideEffect
}