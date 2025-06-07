package com.acon.acon.feature.signin.screen

import com.acon.feature.common.base.BaseContainerHost
import com.acon.acon.domain.error.user.CredentialException
import com.acon.acon.domain.repository.UserRepository
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository
) : com.acon.feature.common.base.BaseContainerHost<SignInUiState, SignInSideEffect>() {

    override val container: Container<SignInUiState, SignInSideEffect> =
        container(initialState = SignInUiState.Loading) {
            isTokenValid()
        }

    fun googleLogin(socialRepository: SocialRepository) = intent {
        socialRepository.googleLogin()
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

    private fun isTokenValid() = intent {
        tokenRepository.getAccessToken().onSuccess { accessToken ->
            if (!accessToken.isNullOrEmpty()) {
                val areaVerificationResult = tokenRepository.getAreaVerification()

                areaVerificationResult.fold(
                    onSuccess = { isVerified ->
                        if (isVerified) {
                            postSideEffect(SignInSideEffect.NavigateToSpotListView)
                        } else {
                            postSideEffect(SignInSideEffect.NavigateToAreaVerification)
                        }
                    },
                    onFailure = {

                    }
                )
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
