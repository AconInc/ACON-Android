package com.acon.acon.feature.settings.screen.composable

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.utils.feature.constants.AppURL
import com.acon.acon.feature.settings.screen.SettingsSideEffect
import com.acon.acon.feature.settings.screen.SettingsViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SettingsScreenContainer(
    modifier: Modifier = Modifier,
    versionName: String,
    onNavigateToProfileScreen: () -> Unit = {},
    onNavigateToOnboardingScreen: () -> Unit = {},
    onNavigateToSignInScreen: () -> Unit = {},
    onNavigateToDeleteAccountScreen: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

    SettingsScreen(
        state = state,
        versionName = versionName,
        modifier = modifier.fillMaxSize(),
        navigateBack = viewModel::navigateBack,
        onTermOfUse = viewModel::onTermOfUse,
        onPrivatePolicy = viewModel::onPrivatePolicy,
        onRetryOnBoarding = viewModel::onRetryOnBoarding,
        onUpdateVersion = viewModel::onUpdateVersion,
        onSignOut = viewModel::onSignOut,
        onDeleteAccountScreen = viewModel::onDeleteAccount,
    )

    viewModel.collectSideEffect {
        when(it) {
            is SettingsSideEffect.NavigateToProfile -> onNavigateToProfileScreen()
            is SettingsSideEffect.OpenPlayStore -> {
                // TODO - 플레이스토어로 이동
            }
            is SettingsSideEffect.OpenTermOfUse -> {
                val url = AppURL.TERM_OF_USE
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
            is SettingsSideEffect.OpenPrivatePolicy -> {
                val url = AppURL.PRIVATE_POLICY
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
            is SettingsSideEffect.NavigateToOnboarding -> onNavigateToOnboardingScreen()
            is SettingsSideEffect.NavigateToSignIn -> onNavigateToSignInScreen()
            is SettingsSideEffect.NavigateToDeleteAccount -> onNavigateToDeleteAccountScreen()
        }
    }
}