package com.acon.acon.feature.spot.screen.spotdetail.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
internal fun StoreImageIndicator(
    pageCount: Int,
    pagerState: PagerState,
    indicatorScrollState: LazyListState,
    modifier: Modifier = Modifier,
    visibleCount: Int = 5
) {
    LaunchedEffect(pagerState.currentPage) {
        val target = when {
            pagerState.currentPage < 1 -> 0
            pagerState.currentPage > pageCount - visibleCount -> pageCount - visibleCount
            else -> pagerState.currentPage - 1
        }
        indicatorScrollState.animateScrollToItem(target.coerceAtLeast(0))
    }

    val displayCount = if (pageCount < visibleCount) pageCount else visibleCount
    val dotSize = 6.dp
    val spacing = 8.dp
    val totalWidth = remember(displayCount) {
        dotSize * displayCount + spacing * (displayCount - 1)
    }

    LazyRow(
        state = indicatorScrollState,
        modifier = modifier.width(totalWidth),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        userScrollEnabled = false
    ) {
        items(pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) AconTheme.color.White else AconTheme.color.Gray300
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color)
                    .size(6.dp)
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