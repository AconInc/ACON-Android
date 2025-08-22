package com.acon.feature.onboarding.introduce.viewmodel

import androidx.compose.runtime.Immutable
import com.acon.acon.core.ui.base.BaseContainerHost
import com.acon.acon.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class IntroduceViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : BaseContainerHost<IntroduceState, Unit>() {

    override val container: Container<IntroduceState, Unit> = container(
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
}

@Immutable
data class IntroduceState(
    val shouldShowLocalReviewScreenAnimation: Boolean = true,
    val shouldShowTop50ScreenAnimation: Boolean = true,
    val shouldShowMainScreenAnimation: Boolean = true
)