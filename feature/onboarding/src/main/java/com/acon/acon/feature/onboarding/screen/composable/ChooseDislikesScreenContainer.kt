package com.acon.acon.feature.onboarding.screen.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.feature.onboarding.screen.ChooseDislikesSideEffect
import com.acon.acon.feature.onboarding.screen.ChooseDislikesViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ChooseDislikesScreenContainer(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChooseDislikesViewModel = hiltViewModel()
) {

    val state by viewModel.collectAsState()

    ChooseDislikesScreen(
        state = state,
        onComplete = viewModel::onCompletion,
        onNoneChosen = viewModel::onNoneClicked,
        onDislikeFoodChosen = viewModel::onDislikeFoodClicked,
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(top = 86.dp, bottom = 16.dp)
            .padding(horizontal = 16.dp)
    )

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is ChooseDislikesSideEffect.NavigateToHome -> onNavigateToHome()
        }
    }
}