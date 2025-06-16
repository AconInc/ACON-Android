package com.acon.acon.feature.profile.composable.screen.galleryGrid.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EndlessLazyVerticalGrid(
    columns: GridCells,
    modifier: Modifier = Modifier,
    listState: LazyGridState = rememberLazyGridState(),
    loading: Boolean = false,
    loadMore: () -> Unit,
    loadingItem: @Composable () -> Unit,
    content: LazyGridScope.() -> Unit
) {
    val reachedBottom by remember {
        derivedStateOf { listState.reachedBottom() }
    }

    LaunchedEffect(reachedBottom) {
        if (reachedBottom && !loading) {
            loadMore()
        }
    }

    LazyVerticalGrid(
        columns = columns,
        state = listState,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        content()

        if (loading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                loadingItem()
            }
        }
    }
}

fun LazyGridState.reachedBottom(buffer: Int = 1): Boolean {
    val layoutInfo = this.layoutInfo
    val totalItems = layoutInfo.totalItemsCount
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem != null && lastVisibleItem.index >= totalItems - buffer
}
