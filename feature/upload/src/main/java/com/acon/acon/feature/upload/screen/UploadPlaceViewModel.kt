package com.acon.acon.feature.upload.screen

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import com.acon.acon.core.model.model.spot.SimpleSpot
import com.acon.acon.core.model.type.CafeOptionType
import com.acon.acon.core.model.type.PriceOptionType
import com.acon.acon.core.model.type.RestaurantFilterType
import com.acon.acon.core.model.type.SpotType
import com.acon.acon.core.ui.base.BaseContainerHost
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class UploadPlaceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseContainerHost<UploadPlaceUiState, UploadPlaceReviewSideEffect>() {

    override val container =
        container<UploadPlaceUiState, UploadPlaceReviewSideEffect>(UploadPlaceUiState()) {

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
    val selectedCategory: PriceOptionType? = null,
    val selectedCafeOption: CafeOptionType? = null,
    val selectedOptionList: List<RestaurantFilterType.RestaurantType> = emptyList(),
    val selectedRestaurantTypes: List<RestaurantFilterType.RestaurantType> = emptyList(),
    val recommendedMenu: String = ""
)

sealed interface UploadPlaceReviewSideEffect {
    data object NavigateBack : UploadPlaceReviewSideEffect
    data class NavigateToComplete(val spot: SimpleSpot) : UploadPlaceReviewSideEffect
}