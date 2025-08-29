package com.acon.feature.onboarding.area.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.model.model.OnboardingPreferences
import com.acon.feature.onboarding.area.viewmodel.VerifyInMapSideEffect
import com.acon.feature.onboarding.area.viewmodel.VerifyInMapViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun VerifyInMapScreenContainer(
    onNavigateToNextScreen: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VerifyInMapViewModel = hiltViewModel()
) {

    val state by viewModel.collectAsState()

    VerifyInMapScreen(
        state = state,
        onCompleteButtonClick = viewModel::onCompleteButtonClicked,
        onBackIconClick = viewModel::onBackIconClicked,
        modifier = modifier
    )

    viewModel.useLiveLocation()
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is VerifyInMapSideEffect.NavigateToNextScreen -> onNavigateToNextScreen()
            VerifyInMapSideEffect.NavigateBack -> onNavigateBack()
        }
    }
}