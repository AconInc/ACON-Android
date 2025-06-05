package com.acon.acon.feature.onboarding.screen

import androidx.compose.runtime.Immutable
import com.acon.acon.domain.repository.OnboardingRepository
import com.acon.feature.common.base.BaseContainerHost
import com.acon.acon.domain.type.FoodType
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class ChooseDislikesViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
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
            if (foodType == FoodType.SEAFOOD) {
                reduce {
                    state.copy(
                        selectedDislikes = if (state.selectedDislikes.contains(FoodType.SEAFOOD)) {
                            state.selectedDislikes - FoodType.SEAFOOD
                        } else {
                            state.selectedDislikes + setOf(
                                FoodType.SEAFOOD,
                                FoodType.SHRIMP,
                                FoodType.CRAB,
                                FoodType.CLAM,
                                FoodType.OYSTER,
                                FoodType.SASHIMI,
                                FoodType.FISH
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
            onboardingRepository.submitOnboardingResult(state.selectedDislikes.toList()).onSuccess {
                postSideEffect(ChooseDislikesSideEffect.NavigateToHome)
            }.onFailure {
                postSideEffect(ChooseDislikesSideEffect.ShowErrorToast)
            }
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
    data object ShowErrorToast : ChooseDislikesSideEffect
}