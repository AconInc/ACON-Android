package com.acon.acon.feature.signin.screen

import com.acon.feature.common.base.BaseContainerHost
import com.acon.acon.domain.error.user.CredentialException
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.type.LocalVerificationType
import com.acon.acon.domain.type.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository
) : com.acon.feature.common.base.BaseContainerHost<SignInUiState, SignInSideEffect>() {

    override val container: Container<SignInUiState, SignInSideEffect> =
        container(initialState = SignInUiState.SignIn)

    fun autoSignIn() = intent {
        userRepository.getUserType().collect {
            when (it) {
                UserType.GUEST -> {
                    Timber.tag(TAG).d("GUEST")
                }

                else -> {
                    Timber.tag(TAG).d("USER")
                    isAreaVerified()
                }
            }
        }
    }

    private fun isAreaVerified() = intent {
        delay(500)
        userRepository.getLocalVerificationType().collect { verificationType ->
            when (verificationType) {
                LocalVerificationType.VERIFIED -> {
                    Timber.tag(TAG).d("LocalVerificationType.VERIFIED")
                    postSideEffect(SignInSideEffect.NavigateToSpotListView)
                }
                LocalVerificationType.NOT_VERIFIED -> {
                    Timber.tag(TAG).d("LocalVerificationType.NOT_VERIFIED")
                    postSideEffect(SignInSideEffect.NavigateToAreaVerification)
                }
            }
        }
    }

    fun googleLogin(socialRepository: SocialRepository) = intent {
        socialRepository.googleLogin()
            .onSuccess {
                if (it.hasVerifiedArea) {
                    userRepository.updateLocalVerificationType(true)
                    postSideEffect(SignInSideEffect.NavigateToSpotListView)
                } else {
                    userRepository.updateLocalVerificationType(false)
                    postSideEffect(SignInSideEffect.NavigateToAreaVerification)
                }
            }
            .onFailure { error ->
                when (error) {
                    is CredentialException.UserCanceled -> {
                        reduce { SignInUiState.SignIn }
                    }

                    is CancellationException -> {
                        reduce { SignInUiState.SignIn }
                    }

                    is CredentialException.NoStoredCredentials -> {
                        reduce { SignInUiState.SignIn }
                    }

                    is SecurityException -> {
                        reduce { SignInUiState.SignIn }
                    }

                    else -> {
                        reduce { SignInUiState.SignIn }
                        postSideEffect(SignInSideEffect.ShowToastMessage)
                    }
                }
            }
    }

    fun navigateToSpotListView() = intent {
        postSideEffect(
            SignInSideEffect.NavigateToSpotListView
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

    companion object {
        const val TAG = "SignInViewModel"
    }
}

sealed interface SignInUiState {
    data object SignIn : SignInUiState
}

sealed interface SignInSideEffect {
    data object ShowToastMessage : SignInSideEffect
    data object NavigateToSpotListView : SignInSideEffect
    data object NavigateToAreaVerification : SignInSideEffect
    data object OnClickTermsOfUse : SignInSideEffect
    data object OnClickPrivacyPolicy : SignInSideEffect
}