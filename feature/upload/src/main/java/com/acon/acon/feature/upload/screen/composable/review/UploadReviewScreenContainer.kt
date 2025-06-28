package com.acon.acon.feature.upload.screen.composable.review

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.ui.android.showToast
import com.acon.acon.core.model.model.spot.SimpleSpot
import com.acon.acon.core.designsystem.R
import com.acon.acon.feature.upload.screen.UploadReviewSideEffect
import com.acon.acon.feature.upload.screen.UploadReviewViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun UploadReviewScreenContainer(
    onNavigateBack: () -> Unit,
    onComplete: (com.acon.acon.core.model.model.spot.SimpleSpot) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UploadReviewViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    UploadReviewScreen(
        state = state,
        onBackAction = viewModel::onBackAction,
        onCompleteAction = viewModel::onCompletion,
        onAcornClick = viewModel::onAcornCountChanged,
        modifier = modifier
    )

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is UploadReviewSideEffect.NavigateBack -> onNavigateBack()
            is UploadReviewSideEffect.NavigateToComplete -> onComplete(effect.spot)
            is UploadReviewSideEffect.ShowToast -> context.showToast(context.getString(R.string.failed_submit_review))
            else -> Unit
        }
    }
}
