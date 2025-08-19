package com.acon.acon.feature.onboarding.screen.composable

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.ui.android.showToast
import com.acon.acon.feature.onboarding.screen.ChooseDislikesSideEffect
import com.acon.acon.feature.onboarding.screen.ChooseDislikesViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ChooseDislikesScreenContainer(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    fromSetting: Boolean = false,
    viewModel: ChooseDislikesViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val state by viewModel.collectAsState()

    ChooseDislikesScreen(
        state = state,
        onComplete = viewModel::onCompletion,
        onNoneChosen = viewModel::onNoneClicked,
        onDismissStopModal = viewModel::dismissStopModal,
        onDislikeFoodChosen = viewModel::onDislikeFoodClicked,
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(top = 86.dp, bottom = 16.dp)
            .padding(horizontal = 16.dp),
    )


    BackHandler(fromSetting) {
        viewModel.showStopModal()
    }

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is ChooseDislikesSideEffect.NavigateToHome -> {
                if (fromSetting) {
                    context.showToast(R.string.success_save)
                }
                onNavigateToHome()
            }
            is ChooseDislikesSideEffect.ShowErrorToast -> context.showToast(R.string.unknown_error_message)
        }
    }
}