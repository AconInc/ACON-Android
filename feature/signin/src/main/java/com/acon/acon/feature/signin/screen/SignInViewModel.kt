package com.acon.acon.feature.signin.screen

import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.error.user.CredentialException
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.type.LocalVerificationType
import com.acon.acon.domain.type.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseContainerHost<SignInUiState, SignInSideEffect>() {

    override val container: Container<SignInUiState, SignInSideEffect> =
        container(initialState = SignInUiState.Loading) {
            autoSignIn()
        }

    private fun autoSignIn() = intent {
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
                        reduce { SignInUiState.Loading }
                    }

                    is CancellationException -> {
                        reduce { SignInUiState.Loading }
                    }

                    is CredentialException.NoStoredCredentials -> {
                        reduce { SignInUiState.Loading }
                    }

                    is SecurityException -> {
                        reduce { SignInUiState.Loading }
                    }

                    else -> {
                        reduce { SignInUiState.Loading }
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
    data class Success(
        val localVerificationType: LocalVerificationType = LocalVerificationType.NOT_VERIFIED
    ) : SignInUiState
    data object Loading : SignInUiState
    data object LoadFailed : SignInUiState
}

sealed interface SignInSideEffect {
    data object ShowToastMessage : SignInSideEffect
    data object NavigateToSpotListView : SignInSideEffect
    data object NavigateToAreaVerification : SignInSideEffect
    data object OnClickTermsOfUse : SignInSideEffect
    data object OnClickPrivacyPolicy : SignInSideEffect
}
