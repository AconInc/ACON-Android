package com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun StoreImageIndicator(
    pageCount: Int,
    pagerState: PagerState,
    indicatorScrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(pagerState.currentPage) {
        val currentPage = pagerState.currentPage
        val size = indicatorScrollState.layoutInfo.visibleItemsInfo.size
        val firstVisibleItemIndex = indicatorScrollState.firstVisibleItemIndex
        val lastVisibleIndex = indicatorScrollState.layoutInfo.visibleItemsInfo.last().index

        if (currentPage > lastVisibleIndex - 1) {
            indicatorScrollState.animateScrollToItem(currentPage - size + 2)
        } else if (currentPage <= firstVisibleItemIndex + 1) {
            indicatorScrollState.animateScrollToItem(Math.max(currentPage - 1, 0))
        }
    }

    LazyRow(
        state = indicatorScrollState,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically,
        userScrollEnabled = false
    ) {
        val visibleIndicators = if (pageCount > 5) 5 else pageCount

        items(visibleIndicators) { index ->
            val selectedIndicator = when {
                pageCount <= 5 -> pagerState.currentPage
                pagerState.currentPage == 4 -> 3
                pagerState.currentPage < 5 -> pagerState.currentPage
                pagerState.currentPage == pageCount - 1 -> 4
                else -> 3
            }

            val size by animateDpAsState(
                targetValue = when {
                    pageCount <= 5 -> 6.dp
                    pagerState.currentPage == 4 -> {
                        if (index == 0 || index == 4) 2.dp else 6.dp
                    }

                    pagerState.currentPage < 5 -> if (index == 4) 2.dp else 6.dp
                    pagerState.currentPage == pageCount - 1 ->
                        when (index) {
                            4 -> 6.dp
                            0 -> 2.dp
                            else -> 6.dp
                        }

                    else -> if (index == 0 || index == 4) 2.dp else 6.dp
                }
            )

            Box(
                modifier = Modifier
                    .size(size)
                    .background(
                        color = if (index == selectedIndicator) AconTheme.color.White else AconTheme.color.Gray300,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Preview
@Composable
private fun StoreImageIndicatorPreview() {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 6 }
    )
    val indicatorScrollState = rememberLazyListState()

    AconTheme {
        StoreImageIndicator(
            pageCount = 6,
            pagerState = pagerState,
            indicatorScrollState
        )
    }
}