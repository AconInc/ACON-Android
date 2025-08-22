package com.acon.acon.feature.signin.screen

import com.acon.acon.core.model.type.UserType
import com.acon.acon.core.ui.base.BaseContainerHost
import com.acon.acon.domain.repository.OnboardingRepository
import com.acon.acon.domain.repository.ProfileRepository
import com.acon.acon.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val onboardingRepository: OnboardingRepository,
    private val userRepository: UserRepository
) : BaseContainerHost<SignInUiState, SignInSideEffect>() {

    override val container: Container<SignInUiState, SignInSideEffect> =
        container(initialState = SignInUiState.SignIn())

    fun signIn() = intent {
        if (userType.value == UserType.GUEST) {
            reduce {
                SignInUiState.SignIn(showSignInInfo = true)
            }
        } else {
            userRepository.getDidOnboarding().onSuccess { did ->
                if (!did)
                    postSideEffect(SignInSideEffect.NavigateToOnboarding)
                else
                    postSideEffect(SignInSideEffect.NavigateToSpotListView)
            }
        }
        userType.collectLatest {
            if (it == UserType.GUEST) {
                reduce {
                    SignInUiState.SignIn(showSignInInfo = true)
                }
            }
        }
    }

    fun navigateToSpotListView() = intent {
        postSideEffect(
            SignInSideEffect.NavigateToSpotListView
        )
    }

    fun onSkipButtonClicked() = intent {
        if (onboardingRepository.getDidOnboarding().getOrDefault(true)) {
            postSideEffect(SignInSideEffect.NavigateToSpotListView)
        } else {
            postSideEffect(SignInSideEffect.NavigateToIntroduce)
        }
    }

    fun navigateToAreaVerification() = intent {
        postSideEffect(
            SignInSideEffect.NavigateToAreaVerification
        )
    }

    fun navigateToOnboarding() = intent {
        postSideEffect(
            SignInSideEffect.NavigateToOnboarding
        )
    }

    fun onClickTermsOfUse() = intent {
        postSideEffect(
            SignInSideEffect.OnClickTermsOfUse
        )
    }

    fun onClickPrivacyPolicy() = intent {
        postSideEffect(
            SignInSideEffect.OnClickPrivacyPolicy
        )
    }
}

sealed interface SignInUiState {
    data class SignIn(
        val showSignInInfo: Boolean = false,
    ) : SignInUiState
}

sealed interface SignInSideEffect {
    data object ShowToastMessage : SignInSideEffect
    data object NavigateToSpotListView : SignInSideEffect
    data object NavigateToAreaVerification : SignInSideEffect
    data object NavigateToOnboarding : SignInSideEffect
    data object OnClickTermsOfUse : SignInSideEffect
    data object OnClickPrivacyPolicy : SignInSideEffect
    data object NavigateToIntroduce : SignInSideEffect
}