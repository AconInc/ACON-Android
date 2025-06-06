package com.acon.acon.feature.profile.composable.screen.profile.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.designsystem.R
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.domain.type.UpdateProfileType
import com.acon.acon.feature.profile.composable.screen.profile.ProfileUiSideEffect
import com.acon.acon.feature.profile.composable.screen.profile.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ProfileScreenContainer(
    socialRepository: SocialRepository,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onNavigateToBookMark: () -> Unit = {},
    onNavigateToSpotListScreen: () -> Unit = {},
    onNavigateToSettingsScreen: () -> Unit = {},
    onNavigateToProfileEditScreen: () -> Unit = {},
    onNavigateToAreaVerificationScreen: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
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
                    viewModel.fetchUserProfileInfo()
                    viewModel.resetUpdateProfileType()
                }
                UpdateProfileType.FAILURE -> {}
            }
        }
    }

    ProfileScreen(
        state = state,
        modifier = modifier.fillMaxSize(),
        onBookmark = viewModel::onBookmark,
        onSettings = viewModel::onSettings,
        onEditProfile = viewModel::onEditProfile,
        onGoogleSignIn = { viewModel.googleLogin(socialRepository) },
        onBottomSheetShowStateChange = viewModel::onBottomSheetShowStateChange,
    )

    viewModel.collectSideEffect {
        when(it) {
            is ProfileUiSideEffect.OnNavigateToBookmarkScreen -> { onNavigateToBookMark() }
            is ProfileUiSideEffect.OnNavigateToSpotListScreen -> { onNavigateToSpotListScreen() }
            is ProfileUiSideEffect.OnNavigateToSettingsScreen -> { onNavigateToSettingsScreen() }
            is ProfileUiSideEffect.OnNavigateToProfileEditScreen -> { onNavigateToProfileEditScreen() }
            is ProfileUiSideEffect.OnNavigateToAreaVerificationScreen -> { onNavigateToAreaVerificationScreen() }
        }
    }
}