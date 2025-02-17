package com.acon.acon.feature.signin.screen

import android.util.Log
import com.acon.acon.core.utils.feature.base.BaseContainerHost
import com.acon.acon.domain.error.user.CredentialException
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.repository.TokenRepository
import com.acon.acon.domain.type.SocialType
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository
) : BaseContainerHost<SignInUiState, SignInSideEffect>() {

    override val container: Container<SignInUiState, SignInSideEffect> =
        container(initialState = SignInUiState.Loading) {
            isTokenValid()
        }

    fun googleLogin(socialRepository: SocialRepository) = intent {
        socialRepository.signIn()
            .onSuccess {
                if(it.hasVerifiedArea) {
                    postSideEffect(SignInSideEffect.NavigateToSpotListView)
                } else {
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
                        tokenRepository.removeGoogleIdToken()
                        reduce { SignInUiState.Loading }
                    }

                    is SecurityException -> {
                        tokenRepository.removeGoogleIdToken()
                        reduce { SignInUiState.Loading }
                    }

                    else -> {
                        tokenRepository.removeGoogleIdToken()
                        reduce { SignInUiState.Loading }
                        postSideEffect(SignInSideEffect.ShowToastMessage)
                    }
                }
            }
    }

    private fun isTokenValid() = intent {
        tokenRepository.getGoogleIdToken().onSuccess { googleIdToken ->
            if (!googleIdToken.isNullOrEmpty()) {
                userRepository.postLogin(SocialType.GOOGLE, googleIdToken)
                    .onSuccess {
                        if(it.hasVerifiedArea) {
                            postSideEffect(SignInSideEffect.NavigateToSpotListView)
                        } else {
                            postSideEffect(SignInSideEffect.NavigateToAreaVerification)
                        }
                    }
                    .onFailure { tokenRepository.removeGoogleIdToken() }
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

}

sealed interface SignInUiState {
    data object Success : SignInUiState
    data object Loading : SignInUiState
    data object LoadFailed: SignInUiState
}

sealed interface SignInSideEffect {
    data object ShowToastMessage : SignInSideEffect
    data object NavigateToSpotListView : SignInSideEffect
    data object NavigateToAreaVerification: SignInSideEffect
    data object OnClickTermsOfUse : SignInSideEffect
    data object OnClickPrivacyPolicy : SignInSideEffect
}
