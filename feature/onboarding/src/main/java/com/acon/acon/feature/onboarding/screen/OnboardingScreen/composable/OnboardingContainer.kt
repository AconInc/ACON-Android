package com.acon.acon.feature.onboarding.screen.OnboardingScreen.composable

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.designsystem.component.dialog.AconTwoButtonDialog
import com.acon.acon.feature.onboarding.screen.OnboardingScreen.OnboardingScreenSideEffect
import com.acon.acon.feature.onboarding.screen.OnboardingScreen.OnboardingViewModel
import com.acon.acon.feature.onboarding.R
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun OnboardingContainer(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
    navigateToLoadingView: () -> Unit = {},
    navigateToSpotListView: () -> Unit = {},
    cancelOnboarding: () -> Unit = {},
    skipOnboarding: () -> Unit = {},
){
    val state = viewModel.collectAsState().value

    OnboardingScreen(
        modifier = modifier,
        screenState = state,
        onCardClicked = viewModel::onCardClicked,
        onBackClicked = viewModel::onBackClicked,
        onSkipClicked = viewModel::showDialog,
        navigateToNextPage = viewModel::navigateToNextPage,
    )

    viewModel.collectSideEffect {
        when(it){
            is OnboardingScreenSideEffect.NavigateToLoadingPage -> {
                navigateToLoadingView()
            }
            OnboardingScreenSideEffect.NavigateToSpotListView -> {
                navigateToSpotListView()
            }
            OnboardingScreenSideEffect.CancelOnboarding -> {
                cancelOnboarding()
            }
            OnboardingScreenSideEffect.SkipOnboarding -> {
                skipOnboarding()
            }
        }
    }

    if (state.showDialog) {
        AconTwoButtonDialog(
            title = stringResource(R.string.onboarding_alert_title),
            content = stringResource(R.string.onboarding_alert_description),
            leftButtonContent = stringResource(R.string.onboarding_alert_left_btn),
            rightButtonContent = stringResource(R.string.onboarding_alert_right_btn),
            contentImage = 0,
            onDismissRequest = {
                viewModel.hideDialog()
            },
            onClickLeft = { // 그만두기
                viewModel.skipConfirmed()
            },
            onClickRight = { // 계속하기
                viewModel.hideDialog()
            },
            isImageEnabled = false
        )
    }
}