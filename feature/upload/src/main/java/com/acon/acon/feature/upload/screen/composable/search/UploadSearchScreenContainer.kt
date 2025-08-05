package com.acon.acon.feature.upload.screen.composable.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.model.model.spot.SimpleSpot
import com.acon.acon.feature.upload.screen.UploadSearchSideEffect
import com.acon.acon.feature.upload.screen.UploadSearchViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun UploadSearchScreenContainer(
    onNavigateBack: () -> Unit,
    onNavigateToEnterMenu: (spot: SimpleSpot) -> Unit,
    onNavigateToPlace: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UploadSearchViewModel = hiltViewModel()
) {

    val state by viewModel.collectAsState()


    UploadSearchScreen(
        state = state,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSearchedSpotClick = viewModel::onSearchedSpotClicked,
        onSuggestionSpotClick = viewModel::onSuggestionSpotClicked,
        onVerifyLocationDialogAction = viewModel::onVerifyLocationDialogAction,
        onUploadPlaceClick = viewModel::moveToUploadPlace,
        onBackAction = viewModel::onBackAction,
        onNextAction = viewModel::onNextAction,
        modifier = modifier
    )
    viewModel.useLiveLocation()
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is UploadSearchSideEffect.NavigateToEnterMenuScreen -> onNavigateToEnterMenu(sideEffect.spot)
            is UploadSearchSideEffect.NavigatePlace -> onNavigateToPlace()
            is UploadSearchSideEffect.NavigateBack -> onNavigateBack()
        }
    }
}