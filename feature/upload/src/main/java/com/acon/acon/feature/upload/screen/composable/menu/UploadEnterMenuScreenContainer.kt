package com.acon.acon.feature.upload.screen.composable.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.model.model.spot.SimpleSpot
import com.acon.acon.feature.upload.screen.UploadEnterMenuSideEffect
import com.acon.acon.feature.upload.screen.UploadEnterMenuViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun UploadEnterMenuScreenContainer(
    onNavigateBack: () -> Unit,
    onNavigateToReview: (spot: SimpleSpot) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UploadEnterMenuViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()

    UploadEnterMenuScreen(
        state = state,
        onBackAction = viewModel::onBackAction,
        onNextAction = viewModel::onNextAction,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        modifier = modifier
    )

    viewModel.collectSideEffect { sideEffect ->
        when(sideEffect) {
            is UploadEnterMenuSideEffect.NavigateToBack -> {
                onNavigateBack()
            }
            is UploadEnterMenuSideEffect.NavigateToReview -> {
                onNavigateToReview(sideEffect.spot)
            }
        }
    }
}