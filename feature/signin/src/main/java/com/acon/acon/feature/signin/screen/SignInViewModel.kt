package com.acon.acon.feature.signin.screen

import com.acon.acon.core.analytics.amplitude.AconAmplitude
import com.acon.acon.core.analytics.constants.EventNames
import com.acon.acon.core.analytics.constants.PropertyKeys
import com.acon.acon.core.model.model.user.VerificationStatus
import com.acon.acon.core.model.type.UserType
import com.acon.acon.core.ui.base.BaseContainerHost
import com.acon.acon.domain.repository.OnboardingRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.core.social.client.SocialAuthClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
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
            onboardingRepository.getOnboardingPreferences().onSuccess {
                if(it.hasTastePreference.not())
                    postSideEffect(SignInSideEffect.NavigateToChooseDislikes)
                else {
                    if (it.shouldShowIntroduce)
                        postSideEffect(SignInSideEffect.NavigateToIntroduce)
                    else
                        postSideEffect(SignInSideEffect.NavigateToSpotListView)
                }
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

    fun onSkipButtonClicked() = intent {
        if (onboardingRepository.getOnboardingPreferences().getOrNull()?.shouldShowIntroduce == true) {
            postSideEffect(SignInSideEffect.NavigateToIntroduce)
        } else {
            postSideEffect(SignInSideEffect.NavigateToSpotListView)
        }
    }

    fun onSignInButtonClicked(socialAuthClient: SocialAuthClient) = intent {
        val platform = socialAuthClient.platform
        val code = socialAuthClient.getCredentialCode()

        userRepository.signIn(platform, code ?: return@intent).onSuccess { verificationStatus ->
            onSignInComplete(verificationStatus)
        }.onFailure {
            postSideEffect(SignInSideEffect.ShowToastMessage)
        }
    }

    private fun onSignInComplete(verificationStatus: VerificationStatus) = intent {
        AconAmplitude.trackEvent(
            eventName = EventNames.SIGN_IN,
            properties = mapOf(
                PropertyKeys.SIGN_IN_OR_NOT to true
            )
        )
        if (verificationStatus.hasVerifiedArea.not()) {
            postSideEffect(SignInSideEffect.NavigateToAreaVerification)
        } else if (verificationStatus.hasPreference.not()) {
            postSideEffect(SignInSideEffect.NavigateToChooseDislikes)
        } else if (onboardingRepository.getOnboardingPreferences().getOrNull()?.shouldShowIntroduce == true) {
            postSideEffect(SignInSideEffect.NavigateToIntroduce)
        } else {
            postSideEffect(SignInSideEffect.NavigateToSpotListView)
        }
        AconAmplitude.setUserId(verificationStatus.externalUUID)
    }

    fun onClickTermsOfUse() = intent {
        postSideEffect(
            SignInSideEffect.OnClickTermsOfUse
        )
    }

    fun navigateToSpotListView() = intent {
        postSideEffect(SignInSideEffect.NavigateToSpotListView)
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
    data object NavigateToChooseDislikes : SignInSideEffect
    data object OnClickTermsOfUse : SignInSideEffect
    data object OnClickPrivacyPolicy : SignInSideEffect
    data object NavigateToIntroduce : SignInSideEffect
}