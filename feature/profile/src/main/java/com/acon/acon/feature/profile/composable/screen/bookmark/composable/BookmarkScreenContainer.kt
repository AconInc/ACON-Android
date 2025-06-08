package com.acon.acon.feature.profile.composable.screen.bookmark.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.feature.profile.composable.screen.bookmark.BookmarkUiSideEffect
import com.acon.acon.feature.profile.composable.screen.bookmark.BookmarkViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun BookmarkScreenContainer(
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit = {},
    onNavigateToSpotDetailScreen: () -> Unit = {},
    viewModel: BookmarkViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()

    BookmarkScreen(
        modifier = modifier,
        state = state,
        onSpotClick = viewModel::onSpotClicked,
        onNavigateToBack = viewModel::navigateToBack
    )

    viewModel.collectSideEffect {
        when(it) {
            BookmarkUiSideEffect.OnNavigateToBack -> { onNavigateToBack() }
            BookmarkUiSideEffect.OnNavigateToSpotDetailScreen -> { onNavigateToSpotDetailScreen() }
        }
    }
}