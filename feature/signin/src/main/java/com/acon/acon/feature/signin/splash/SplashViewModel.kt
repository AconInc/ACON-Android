package com.acon.acon.feature.signin.splash

import com.acon.feature.common.base.BaseContainerHost
import com.acon.acon.domain.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
) : BaseContainerHost<SplashUiState, SplashSideEffect>() {

    override val container = container<SplashUiState, SplashSideEffect>(SplashUiState.StartSplash) {}

     fun isTokenValid() = intent {
         tokenRepository.getAccessToken().onSuccess { accessToken ->
             if (!accessToken.isNullOrEmpty()) {
                 postSideEffect(SplashSideEffect.NavigationToSpotListView)
             } else {
                 postSideEffect(SplashSideEffect.NavigationToSignIn)
             }
         }.onFailure {
             postSideEffect(SplashSideEffect.NavigationToSignIn)
         }
    }

}

sealed interface SplashUiState {
    data object StartSplash: SplashUiState
}
sealed interface SplashSideEffect {
    data object NavigationToSignIn : SplashSideEffect
    data object NavigationToSpotListView : SplashSideEffect
}