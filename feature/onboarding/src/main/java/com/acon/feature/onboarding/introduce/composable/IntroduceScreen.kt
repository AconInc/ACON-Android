package com.acon.feature.onboarding.introduce.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.ui.base.ScreenProvider
import com.acon.acon.core.ui.compose.disableSwipeAnimation
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun IntroduceScreen(
    pagerState: PagerState,
    introduceScreenProviders: ImmutableList<ScreenProvider>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(top = 24.dp)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(pagerState.pageCount) {
                Spacer(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = if (pagerState.currentPage == it) AconTheme.color.White else AconTheme.color.Gray300,
                            shape = CircleShape
                        )
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = pagerState.pageCount,
            modifier = Modifier.fillMaxWidth().disableSwipeAnimation(
                pagerState = pagerState,
                flingThreshold = 400f
            ),
            userScrollEnabled = false,
        ) { page ->
            introduceScreenProviders[page].provide()
        }
    }
}

