package com.acon.acon.feature.upload.screen

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.acon.acon.core.model.model.upload.SearchedSpotByMap
import com.acon.acon.core.model.type.CafeOptionType
import com.acon.acon.core.model.type.PriceOptionType
import com.acon.acon.core.model.type.RestaurantFilterType
import com.acon.acon.core.model.type.SpotType
import com.acon.acon.core.ui.base.BaseContainerHost
import com.acon.acon.domain.repository.MapSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class UploadPlaceViewModel @Inject constructor(
    private val mapSearchRepository: MapSearchRepository
) : BaseContainerHost<UploadPlaceUiState, UploadPlaceSideEffect>() {

    override val container =
        container<UploadPlaceUiState, UploadPlaceSideEffect>(UploadPlaceUiState()) {
            viewModelScope.launch {
                queryFlow
                    .debounce(100)
                    .distinctUntilChanged()
                    .collect { query ->
                        if (query.isBlank()) {
                            reduce {
                                state.copy(
                                    recommendMenu = "",
                                )
                            }
                        } else {
                            reduce {
                                state.copy(
                                    recommendMenu = query,
                                )
                            }
                        }
                    }
            }

            viewModelScope.launch {
                queryFlow
                    .debounce(100)
                    .distinctUntilChanged()
                    .collect { query ->
                        if (query.isBlank()) {
                            reduce {
                                state.copy(
                                    searchedSpotsByMap = emptyList(),
                                    showSearchedSpotsByMap = false,
                                    isNextBtnEnabled = false
                                )
                            }
                        } else {
                            mapSearchRepository.fetchMapSearch(query).onSuccess {
                                reduce {
                                    state.copy(
                                        searchedSpotsByMap = it
                                    )
                                }
                            }.onFailure {  }
                        }
                    }
            }
        }

    private val queryFlow = MutableStateFlow("")

    fun onSearchQueryChanged(query: String) = intent {
        queryFlow.value = query
    }

    fun onSearchQueryOrSelectionChanged(query: String, isSelection: Boolean) = intent {
        reduce {
            if (isSelection)
                state.copy(
                    showSearchedSpotsByMap = false
                )
            else
                state.copy(
                    selectedSpotByMap = null,
                    showSearchedSpotsByMap = query.isNotBlank()
                )
        }
        queryFlow.value = query
    }

    fun onSearchSpotByMapClicked(searchedSpotByMap: SearchedSpotByMap, onSuccess: () -> Unit) = intent {
        onSuccess()
        reduce {
            state.copy(
                selectedSpotByMap = searchedSpotByMap,
                showSearchedSpotsByMap = false,
                isNextBtnEnabled = true
            )
        }
    }

    fun onPreviousBtnDisabled() = intent {
        reduce { state.copy(isPreviousBtnEnabled = false)}
    }

    fun onPreviousBtnEnabled() = intent {
        reduce { state.copy(isPreviousBtnEnabled = true)}
    }

    fun updateNextBtnEnabled(isEnabled: Boolean) = intent {
        reduce { state.copy(isNextBtnEnabled = isEnabled)}
    }

    fun updateSpotType(spotType: SpotType) = intent {
        reduce { state.copy(selectedSpotType = spotType) }
    }

    fun updateCafeOptionType(cafeOption: CafeOptionType) = intent {
        reduce { state.copy(selectedCafeOption = cafeOption) }
    }

    fun updatePriceOptionType(priceOption: PriceOptionType) = intent {
        reduce { state.copy(selectedPriceOption = priceOption) }
    }

    fun updateRestaurantType(type: RestaurantFilterType.RestaurantType) = intent {
        reduce {
            val currentSelectedTypes = state.selectedRestaurantTypes.toMutableList()

            if (currentSelectedTypes.contains(type)) {
                currentSelectedTypes.remove(type)
            } else {
                currentSelectedTypes.add(type)
            }
            state.copy(selectedRestaurantTypes = currentSelectedTypes)
        }
    }

    fun onAddImageUris(uris: List<Uri>) = intent {
        reduce {
            val currentUris = state.selectedImageUris ?: emptyList()
            val canAddCount = state.maxImageCount - currentUris.size

            if (canAddCount <= 0) {
                state
            } else {
                val toAdd = uris.take(canAddCount)
                state.copy(selectedImageUris = currentUris.plus(toAdd))
            }
        }
    }

    fun onRemoveImageUri(uri: Uri) = intent {
        val currentUris = state.selectedImageUris?.toMutableList()
        val removedSuccessfully = currentUris?.remove(uri)

        if (removedSuccessfully == true) {
            reduce {
                state.copy(selectedImageUris = currentUris)
            }
        }
    }

    fun onRequestRemoveUploadPlaceImageDialog(uri: Uri) = intent {
        reduce {
            state.copy(
                showRemoveUploadPlaceImageDialog = true,
                selectedUriToRemove = uri
            )
        }
    }

    fun onDismissRemoveUploadPlaceImageDialog() = intent {
        reduce {
            state.copy(
                showRemoveUploadPlaceImageDialog = false,
                selectedUriToRemove = null
            )
        }
    }

    fun goToNextStep(lastStepIndex: Int) = intent {
        if (state.currentStep < lastStepIndex) {
            reduce {
                state.copy(currentStep = state.currentStep + 1)
            }
        }
    }

    fun goToPreviousStep() = intent {
        if (state.currentStep > 0) {
            reduce {
                state.copy(currentStep = state.currentStep - 1)
            }
        }
    }

    fun onRequestExitUploadPlaceDialog() = intent {
        reduce {
            state.copy(showExitUploadPlaceDialog = true)
        }
    }

    fun onDismissExitUploadPlaceDialog() = intent {
        reduce {
            state.copy(showExitUploadPlaceDialog = false)
        }
    }
}

@Immutable
data class UploadPlaceUiState(
    val isPreviousBtnEnabled: Boolean = false,
    val isNextBtnEnabled: Boolean = false,
    val showExitUploadPlaceDialog: Boolean = false,
    val showRemoveUploadPlaceImageDialog: Boolean = false,
    val selectedSpotByMap: SearchedSpotByMap? = null,
    val searchedSpotsByMap: List<SearchedSpotByMap> = listOf(),
    val showSearchedSpotsByMap: Boolean = false,
    val selectedUriToRemove: Uri? = null,
    val selectedSpotType: SpotType? = null,
    val selectedPriceOption: PriceOptionType? = null,
    val selectedCafeOption: CafeOptionType? = null,
    val selectedOptionList: List<RestaurantFilterType.RestaurantType> = emptyList(),
    val selectedRestaurantTypes: List<RestaurantFilterType.RestaurantType> = emptyList(),
    val recommendMenu: String? = "",
    val selectedImageUris: List<Uri>? = emptyList(),
    val maxImageCount: Int = 10,
    val currentStep: Int = 0
)

sealed interface UploadPlaceSideEffect