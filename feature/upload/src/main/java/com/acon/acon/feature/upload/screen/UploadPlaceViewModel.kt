package com.acon.acon.feature.upload.screen

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.acon.acon.core.model.type.CafeOptionType
import com.acon.acon.core.model.type.PriceOptionType
import com.acon.acon.core.model.type.RestaurantFilterType
import com.acon.acon.core.model.type.SpotType
import com.acon.acon.core.ui.base.BaseContainerHost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class, FlowPreview::class)@HiltViewModel
class UploadPlaceViewModel @Inject constructor(

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
        }

    private val queryFlow = MutableStateFlow("")

    fun onSearchQueryChanged(query: String) = intent {
        queryFlow.value = query
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

    fun onRemoveImageUri(uri: Uri, currentPageIndex: Int) = intent {
        val currentUris = state.selectedImageUris?.toMutableList()
        val removedSuccessfully = currentUris?.remove(uri)

        if (removedSuccessfully == true) {
            reduce {
                state.copy(selectedImageUris = currentUris)
            }

            val targetPageIndex = if (currentUris.isEmpty()) {
                0
            } else {
                currentPageIndex.coerceAtMost(currentUris.size - 1)
            }
            postSideEffect(UploadPlaceSideEffect.ImageRemoved(removedPage = targetPageIndex))
        }
    }

    fun showExitUploadPlaceDialog() = intent {
        reduce {
            state.copy(showExitUploadPlaceDialog = true)
        }
    }

    fun dismissExitUploadPlaceDialog() = intent {
        reduce {
            state.copy(showExitUploadPlaceDialog = false)
        }
    }
}

// featureList - 장소 특징 목록
//val selectedSpotType: SpotType
//val selectedRestaurantFilters: Map<FilterDetailKey, Set<RestaurantFilterType>>
@Immutable
data class UploadPlaceUiState(
    val isPreviousBtnEnabled: Boolean = false,
    val isNextBtnEnabled: Boolean = false,
    val showExitUploadPlaceDialog: Boolean = false,
    val selectedSpotName: String = "",
    val selectedSpotAddress: String = "",
    val selectedSpotType: SpotType? = null,
    val selectedPriceOption: PriceOptionType? = null,
    val selectedCafeOption: CafeOptionType? = null,
    val selectedOptionList: List<RestaurantFilterType.RestaurantType> = emptyList(),
    val selectedRestaurantTypes: List<RestaurantFilterType.RestaurantType> = emptyList(),
    val recommendMenu: String? = "",
    val selectedImageUris: List<Uri>? = emptyList(),
    val maxImageCount: Int = 10
)

sealed interface UploadPlaceSideEffect {
    data object NavigateBack : UploadPlaceSideEffect
    data class ImageRemoved(val removedPage: Int) : UploadPlaceSideEffect
}