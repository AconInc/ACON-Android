package com.acon.acon.feature.signin.splash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SplashScreenContainer(
    navigationToSignIn: () -> Unit,
    navigationToSpotListView: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()

    SplashScreen(
        state = state,
        onAnimationEnd = { viewModel.isTokenValid() },
        modifier = modifier
            .fillMaxSize()
    )

    viewModel.collectSideEffect { sideEffect ->
        when(sideEffect) {
            is SplashSideEffect.NavigationToSignIn -> { navigationToSignIn() }
            is SplashSideEffect.NavigationToSpotListView -> { navigationToSpotListView() }
        }
    }

}
