package com.acon.feature.onboarding.introduce.composable

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.toImmutableList

@Composable
fun IntroduceScreenContainer(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {

    val introduceScreenProviders = remember {
        mutableStateListOf(
            IntroduceLocalReviewScreenProvider(),
            IntroduceTop50ScreenProvider(),
            IntroduceMainScreenProvider(onNavigateToHome)
        )
    }

    val pagerState = rememberPagerState { introduceScreenProviders.size }

    IntroduceScreen(
        modifier = modifier,
        pagerState = pagerState,
        introduceScreenProviders = introduceScreenProviders.toImmutableList(),
    )
}
