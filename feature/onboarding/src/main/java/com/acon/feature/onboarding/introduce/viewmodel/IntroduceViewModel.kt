package com.acon.feature.onboarding.introduce.viewmodel

import androidx.compose.runtime.Immutable
import com.acon.acon.core.ui.base.BaseContainerHost
import com.acon.acon.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class IntroduceViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : BaseContainerHost<IntroduceState, IntroduceSideEffect>() {

    override val container = container<IntroduceState, IntroduceSideEffect>(
        initialState = IntroduceState()
    )

    fun onIntroduceLocalReviewScreenDisposed() = intent {
        reduce {
            state.copy(
                shouldShowLocalReviewScreenAnimation = false
            )
        }
    }

    fun onIntroduceTop50ScreenDisposed() = intent {
        reduce {
            state.copy(
                shouldShowTop50ScreenAnimation = false
            )
        }
    }

    fun onIntroduceMainScreenDisposed() = intent {
        reduce {
            state.copy(
                shouldShowMainScreenAnimation = false
            )
        }
    }

    fun onStartButtonClicked() = intent {
        onboardingRepository.saveDidOnboarding(true)
        postSideEffect(IntroduceSideEffect.OnNavigateToHomeScreen)
    }
}

@Immutable
data class IntroduceState(
    val shouldShowLocalReviewScreenAnimation: Boolean = true,
    val shouldShowTop50ScreenAnimation: Boolean = true,
    val shouldShowMainScreenAnimation: Boolean = true
)

sealed interface IntroduceSideEffect {
    data object OnNavigateToHomeScreen : IntroduceSideEffect
}