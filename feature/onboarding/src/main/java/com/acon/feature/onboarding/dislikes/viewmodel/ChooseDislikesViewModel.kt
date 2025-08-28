package com.acon.feature.onboarding.dislikes.viewmodel

import androidx.compose.runtime.Immutable
import com.acon.acon.domain.repository.OnboardingRepository
import com.acon.acon.core.model.type.FoodType
import com.acon.acon.core.ui.base.BaseContainerHost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    fun showStopModal() = intent {
        runOn<ChooseDislikesUiState.Success> {
            reduce {
                state.copy(
                    showStopModal = true
                )
            }
        }
    }

    fun dismissStopModal(onComplete: () -> Unit) = intent {
        runOn<ChooseDislikesUiState.Success> {
            reduce {
                state.copy(
                    showStopModal = false
                )
            }
        }
        withContext(Dispatchers.Main) {
            onComplete()
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
                if (onboardingRepository.getDidOnboarding().getOrDefault(true))
                    postSideEffect(ChooseDislikesSideEffect.NavigateToHome)
                else
                    postSideEffect(ChooseDislikesSideEffect.NavigateToIntroduce)
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
        val showStopModal: Boolean = false
    ) : ChooseDislikesUiState
}

sealed interface ChooseDislikesSideEffect {
    data object NavigateToHome : ChooseDislikesSideEffect
    data object ShowErrorToast : ChooseDislikesSideEffect
    data object NavigateToIntroduce : ChooseDislikesSideEffect
}