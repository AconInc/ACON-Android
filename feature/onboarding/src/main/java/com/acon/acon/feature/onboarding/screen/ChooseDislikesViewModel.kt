package com.acon.acon.feature.onboarding.screen

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

    fun onDislikeFoodClicked(foodType: com.acon.acon.core.model.type.FoodType) = intent {
        runOn<ChooseDislikesUiState.Success> {
            if (foodType == com.acon.acon.core.model.type.FoodType.SEAFOOD) {
                reduce {
                    state.copy(
                        selectedDislikes = if (state.selectedDislikes.contains(com.acon.acon.core.model.type.FoodType.SEAFOOD)) {
                            state.selectedDislikes - com.acon.acon.core.model.type.FoodType.SEAFOOD
                        } else {
                            state.selectedDislikes + setOf(
                                com.acon.acon.core.model.type.FoodType.SEAFOOD,
                                com.acon.acon.core.model.type.FoodType.SHRIMP,
                                com.acon.acon.core.model.type.FoodType.CRAB,
                                com.acon.acon.core.model.type.FoodType.CLAM,
                                com.acon.acon.core.model.type.FoodType.OYSTER,
                                com.acon.acon.core.model.type.FoodType.SASHIMI,
                                com.acon.acon.core.model.type.FoodType.FISH
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
        val selectedDislikes: Set<com.acon.acon.core.model.type.FoodType> = emptySet(),
        val showStopModal: Boolean = false
    ) : ChooseDislikesUiState
}

sealed interface ChooseDislikesSideEffect {
    data object NavigateToHome : ChooseDislikesSideEffect
    data object ShowErrorToast : ChooseDislikesSideEffect
}