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
import com.acon.core.ui.android.showToast
import com.acon.core.type.UpdateProfileType
import com.acon.acon.feature.profile.composable.screen.profile.ProfileUiSideEffect
import com.acon.acon.feature.profile.composable.screen.profile.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ProfileScreenContainer(
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
                UpdateProfileType.IDLE -> {}
                UpdateProfileType.SUCCESS -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = snackbarMsg,
                            duration = SnackbarDuration.Short,
                        )
                    }
                    viewModel.resetUpdateProfileType()
                }
                UpdateProfileType.FAILURE -> {}
            }
        }
    }

    ProfileScreen(
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
            is ProfileUiSideEffect.OnNavigateToSpotDetailScreen -> { onNavigateToSpotDetailScreen(it.spotId) }
            is ProfileUiSideEffect.OnNavigateToBookmarkScreen -> { onNavigateToBookMarkScreen() }
            is ProfileUiSideEffect.OnNavigateToSpotListScreen -> { onNavigateToSpotListScreen() }
            is ProfileUiSideEffect.OnNavigateToSettingsScreen -> { onNavigateToSettingsScreen() }
            is ProfileUiSideEffect.OnNavigateToProfileEditScreen -> { onNavigateToProfileEditScreen() }
            is ProfileUiSideEffect.FailedToLoadProfileInfo -> { context.showToast(R.string.unknown_error) }
        }
    }
}