package com.acon.feature.onboarding.introduce.composable

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.feature.onboarding.introduce.viewmodel.IntroduceViewModel
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun IntroduceScreenContainer(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IntroduceViewModel = hiltViewModel()
) {

    val state by viewModel.collectAsState()
    val introduceScreenProviders = remember {
        mutableStateListOf(
            IntroduceLocalReviewScreenProvider(
                animationEnabled = { state.shouldShowLocalReviewScreenAnimation }
            ),
            IntroduceTop50ScreenProvider(
                onRendered = viewModel::onIntroduceTop50ScreenRendered,
                animationEnabled = { state.shouldShowTop50ScreenAnimation }
            ),
            IntroduceMainScreenProvider(
                onStartButtonClick = onNavigateToHome,
                onRendered = viewModel::onIntroduceMainScreenRendered,
                onDisposed = viewModel::onIntroduceMainScreenDisposed,
                animationEnabled = { state.shouldShowMainScreenAnimation }
            )
        )
    }

    val pagerState = rememberPagerState { introduceScreenProviders.size }

    IntroduceScreen(
        modifier = modifier,
        pagerState = pagerState,
        introduceScreenProviders = introduceScreenProviders.toImmutableList(),
    )
}
