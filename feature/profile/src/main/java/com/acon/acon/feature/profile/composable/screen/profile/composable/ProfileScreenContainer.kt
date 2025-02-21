package com.acon.acon.feature.profile.composable.screen.profile.composable

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.utils.feature.constants.AppURL
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.feature.profile.R
import com.acon.acon.feature.profile.composable.screen.profile.ProfileUiSideEffect
import com.acon.acon.feature.profile.composable.screen.profile.ProfileViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ProfileScreenContainer(
    socialRepository: SocialRepository,
    profileUpdateResult: String,
    modifier: Modifier = Modifier,
    onNavigateToSpotListScreen: () -> Unit = {},
    onNavigateToSettingsScreen: () -> Unit = {},
    onNavigateToProfileEditScreen: () -> Unit = {},
    onNavigateToAreaVerificationScreen: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

    ProfileScreen(
        state = state,
        profileUpdateResult = profileUpdateResult,
        modifier = modifier.fillMaxSize(),
        onSettings = viewModel::onSettings,
        onEditProfile = viewModel::onEditProfile,
        onGoogleSignIn = { viewModel.googleLogin(socialRepository) },
        onTermOfUse = viewModel::onTermOfUse,
        onPrivatePolicy = viewModel::onPrivatePolicy,
        onBottomSheetShowStateChange = viewModel::onBottomSheetShowStateChange,
    )

    val success = stringResource(R.string.success)
    LaunchedEffect(profileUpdateResult) {
        if(profileUpdateResult == success) {
            viewModel.fetchUserProfileInfo()
        }
    }

    viewModel.collectSideEffect {
        when(it) {
            is ProfileUiSideEffect.OnNavigateToSpotListScreen -> { onNavigateToSpotListScreen() }
            is ProfileUiSideEffect.OnNavigateToSettingsScreen -> { onNavigateToSettingsScreen() }
            is ProfileUiSideEffect.OnNavigateToProfileEditScreen -> { onNavigateToProfileEditScreen() }
            is ProfileUiSideEffect.OnNavigateToAreaVerificationScreen -> { onNavigateToAreaVerificationScreen() }
            is ProfileUiSideEffect.OnPrivatePolicy -> {
                val url = AppURL.TERM_OF_USE
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
            is ProfileUiSideEffect.OnTermOfUse -> {
                val url = AppURL.PRIVATE_POLICY
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        }
    }

}