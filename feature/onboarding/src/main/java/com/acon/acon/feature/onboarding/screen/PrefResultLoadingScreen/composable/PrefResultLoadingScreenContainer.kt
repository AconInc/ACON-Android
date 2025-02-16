package com.acon.acon.feature.onboarding.screen.PrefResultLoadingScreen.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.feature.onboarding.screen.PrefResultLoadingScreen.PrefResultLoadingScreenSideEffect
import com.acon.acon.feature.onboarding.screen.PrefResultLoadingScreen.PrefResultLoadingScreenViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun PrefResultLoadingScreenContainer(
    modifier: Modifier = Modifier,
    viewModel: PrefResultLoadingScreenViewModel = hiltViewModel(),
    navigateToSpotListView: () -> Unit = {},
){
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect {
        when(it) {
            PrefResultLoadingScreenSideEffect.navigateToSpotListView -> {
                navigateToSpotListView()
            }
        }
    }

    PrefResultLoadingScreen(
        modifier = modifier,
        screenState = state,
        navigateToSpotListView = navigateToSpotListView,
    )
}