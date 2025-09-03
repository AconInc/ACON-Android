package com.acon.acon.feature.profile.composable.screen.profile.composable

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.ui.android.showToast
import com.acon.acon.feature.profile.composable.screen.profile.ProfileUiSideEffectLegacy
import com.acon.acon.feature.profile.composable.screen.profile.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ProfileScreenContainerLegacy(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onNavigateToSpotDetailScreen: (Long) -> Unit = {},
    onNavigateToBookMarkScreen: () -> Unit = {},
    onNavigateToSpotListScreen: () -> Unit = {},
    onNavigateToSettingsScreen: () -> Unit = {},
    onNavigateToProfileEditScreen: () -> Unit = {},
    onNavigateToUploadScreen: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val snackbarMsg = stringResource(R.string.profile_save_success)

    LaunchedEffect(viewModel.updateProfileState) {
        viewModel.updateProfileState.collectLatest {
            when(it) {
                com.acon.acon.core.model.type.UpdateProfileType.IDLE -> {}
                com.acon.acon.core.model.type.UpdateProfileType.SUCCESS -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = snackbarMsg,
                            duration = SnackbarDuration.Short,
                        )
                    }
                    viewModel.resetUpdateProfileType()
                }
                com.acon.acon.core.model.type.UpdateProfileType.FAILURE -> {}
            }
        }
    }

    ProfileScreenLegacy(
        state = state,
        modifier = modifier,
        onBookmark = viewModel::onBookmark,
        onSettings = viewModel::onSettings,
        onSpotDetail = viewModel::onSpotDetail,
        onEditProfile = viewModel::onEditProfile,
        onNavigateToSpotListScreen = onNavigateToSpotListScreen,
        onNavigateToUploadScreen = onNavigateToUploadScreen
    )

    viewModel.useUserType()
    viewModel.collectSideEffect {
        when(it) {
            is ProfileUiSideEffectLegacy.OnNavigateToSpotDetailScreen -> { onNavigateToSpotDetailScreen(it.spotId) }
            is ProfileUiSideEffectLegacy.OnNavigateToBookmarkScreen -> { onNavigateToBookMarkScreen() }
            is ProfileUiSideEffectLegacy.OnNavigateToSpotListScreen -> { onNavigateToSpotListScreen() }
            is ProfileUiSideEffectLegacy.OnNavigateToSettingsScreen -> { onNavigateToSettingsScreen() }
            is ProfileUiSideEffectLegacy.OnNavigateToProfileEditScreenLegacy -> { onNavigateToProfileEditScreen() }
            is ProfileUiSideEffectLegacy.FailedToLoadProfileInfoLegacy -> { context.showToast(R.string.unknown_error) }
        }
    }
}