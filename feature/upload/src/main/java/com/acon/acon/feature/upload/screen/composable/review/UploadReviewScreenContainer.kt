package com.acon.acon.feature.upload.screen.composable.review

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.domain.model.spot.SimpleSpot
import com.acon.acon.feature.upload.screen.UploadReviewSideEffect
import com.acon.acon.feature.upload.screen.UploadReviewViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun UploadReviewScreenContainer(
    onNavigateBack: () -> Unit,
    onComplete: (SimpleSpot) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UploadReviewViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()

    UploadReviewScreen(
        state = state,
        onBackAction = viewModel::onBackAction,
        onCompleteAction = viewModel::onCompletion,
        onDotoriClick = viewModel::onDotoriCountChanged,
        modifier = modifier
    )

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is UploadReviewSideEffect.NavigateBack -> onNavigateBack()
            is UploadReviewSideEffect.NavigateToComplete -> onComplete(effect.spot)
            else -> Unit
        }
    }
}
