package com.acon.acon.feature.onboarding.screen

import androidx.compose.runtime.Immutable
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.type.FoodType
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class ChooseDislikesViewModel @Inject constructor(

) : BaseContainerHost<ChooseDislikesUiState, ChooseDislikesSideEffect>() {

    override val container = container<ChooseDislikesUiState, ChooseDislikesSideEffect>(ChooseDislikesUiState.Success()) {

    }

    fun onNoneClicked() = intent {
        runOn<ChooseDislikesUiState.Success> {
            val isNoneSelectedToBe = !state.isNoneChosen
            reduce {
                state.copy(
                    isNoneChosen = isNoneSelectedToBe,
                    selectedDislikes = if (isNoneSelectedToBe) emptySet() else state.selectedDislikes
                )
            }
        }
    }

    fun onDislikeFoodClicked(foodType: FoodType) = intent {
        runOn<ChooseDislikesUiState.Success> {
            if (foodType == FoodType.Seafood) {
                reduce {
                    state.copy(
                        selectedDislikes = if (state.selectedDislikes.contains(FoodType.Seafood)) {
                            state.selectedDislikes - FoodType.Seafood
                        } else {
                            state.selectedDislikes + setOf(
                                FoodType.Seafood,
                                FoodType.Shrimp,
                                FoodType.Crab,
                                FoodType.Clam,
                                FoodType.Oyster,
                                FoodType.RawFish,
                                FoodType.Fish
                            )
                        },
                    )
                }
            } else {
                reduce {
                    state.copy(
                        selectedDislikes = if (state.selectedDislikes.contains(foodType)) {
                            state.selectedDislikes - foodType
                        } else {
                            state.selectedDislikes + foodType
                        }
                    )
                }
            }
        }
    }

    fun onCompletion() = intent {
        runOn<ChooseDislikesUiState.Success> {
            postSideEffect(ChooseDislikesSideEffect.NavigateToHome)
        }
    }
}

sealed interface ChooseDislikesUiState {
    @Immutable
    data class Success(
        val isNoneChosen: Boolean = false,
        val selectedDislikes: Set<FoodType> = emptySet(),
    ) : ChooseDislikesUiState
}

sealed interface ChooseDislikesSideEffect {
    data object NavigateToHome : ChooseDislikesSideEffect
}