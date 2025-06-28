package com.acon.acon.feature.settings.screen.composable

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.common.UrlConstants
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.ui.android.showToast
import com.acon.acon.feature.settings.screen.SettingsSideEffect
import com.acon.acon.feature.settings.screen.SettingsViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SettingsScreenContainer(
    versionName: String,
    modifier: Modifier = Modifier,
    onNavigateToSignInScreen: () -> Unit = {},
    onNavigateToProfileScreen: () -> Unit = {},
    onNavigateToOnboardingScreen: () -> Unit = {},
    onNavigateLocalVerificationScreen: () -> Unit = {},
    onNavigateToDeleteAccountScreen: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

    //TODO - 임시변수
    val isLatestVersion = true

    SettingsScreen(
        state = state,
        isLatestVersion = isLatestVersion,
        modifier = modifier.fillMaxSize(),
        onLogoutDialogShowStateChange = viewModel::onLogoutDialogShowStateChange,
        navigateBack = viewModel::navigateBack,
        onTermOfUse = viewModel::onTermOfUse,
        onPrivatePolicy = viewModel::onPrivatePolicy,
        onRetryOnBoarding = viewModel::onRetryOnBoarding,
        onAreaVerification = viewModel::onNavigateToLocalVerification,
        onUpdateVersion = viewModel::onUpdateVersion,
        onSignOut = viewModel::onSignOut,
        onDeleteAccountScreen = viewModel::onDeleteAccount,
    )

    viewModel.collectSideEffect {
        when(it) {
            is SettingsSideEffect.ShowToastMessage -> { context.showToast(R.string.toast_logout_failed) }
            is SettingsSideEffect.NavigateToSignIn -> onNavigateToSignInScreen()
            is SettingsSideEffect.NavigateToProfile -> onNavigateToProfileScreen()
            is SettingsSideEffect.OpenPlayStore -> {
                // TODO - 플레이스토어로 이동
            }
            is SettingsSideEffect.OpenTermOfUse -> {
                val url = UrlConstants.TERM_OF_USE
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
            is SettingsSideEffect.OpenPrivatePolicy -> {
                val url = UrlConstants.PRIVATE_POLICY
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
            is SettingsSideEffect.NavigateToOnboarding -> onNavigateToOnboardingScreen()
            is SettingsSideEffect.NavigateToLocalVerification -> onNavigateLocalVerificationScreen()
            is SettingsSideEffect.NavigateToDeleteAccount -> onNavigateToDeleteAccountScreen()
        }
    }
}