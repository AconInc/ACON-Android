package com.acon.acon.feature.upload.screen.composable.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.domain.model.upload.v2.SearchedSpot
import com.acon.acon.feature.upload.screen.UploadSearchSideEffect
import com.acon.acon.feature.upload.screen.UploadSearchViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun UploadSearchScreenContainer(
    onNavigateBack: () -> Unit,
    onNavigateToReview: (spot: SearchedSpot) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UploadSearchViewModel = hiltViewModel()
) {

    val state by viewModel.collectAsState()

    UploadSearchScreen(
        state = state,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onBackAction = viewModel::onBackAction,
        onNextAction = viewModel::onNextAction,
        modifier = modifier
    )

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is UploadSearchSideEffect.NavigateToReviewScreen -> onNavigateToReview(sideEffect.spot)
            is UploadSearchSideEffect.NavigateBack -> onNavigateBack()
        }
    }
}