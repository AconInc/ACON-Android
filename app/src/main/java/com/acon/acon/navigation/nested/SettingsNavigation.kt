package com.acon.acon.navigation.nested

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.acon.acon.BuildConfig
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.SettingsRoute
import com.acon.acon.feature.areaverification.AreaVerificationRoute
import com.acon.acon.feature.onboarding.OnboardingRoute
import com.acon.acon.feature.profile.composable.ProfileRoute
import com.acon.acon.feature.settings.screen.composable.SettingsScreenContainer
import com.acon.acon.feature.signin.screen.SignInRoute
import com.acon.acon.feature.verification.screen.composable.LocalVerificationScreenContainer
import com.acon.acon.feature.withdraw.screen.composable.DeleteAccountScreenContainer

internal fun NavGraphBuilder.settingsNavigation(
    navController: NavHostController
) {
    val versionName = BuildConfig.VERSION_NAME

    navigation<SettingsRoute.Graph>(
        startDestination = SettingsRoute.Settings,
    ) {
        composable<SettingsRoute.Settings> {
            SettingsScreenContainer(
                modifier = Modifier.fillMaxSize(),
                versionName = versionName,
                onNavigateToProfileScreen = {
                    navController.navigate(ProfileRoute.Profile) {
                        popUpTo(SettingsRoute.Graph) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToOnboardingScreen = {
                    navController.navigate(OnboardingRoute.Graph)
                },
                onNavigateLocalVerificationScreen = {
                    navController.navigate(SettingsRoute.LocalVerification)
                },
                onNavigateToSignInScreen = {
                    navController.navigate(SignInRoute.SignIn) {
                        popUpTo(SettingsRoute.Graph) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToDeleteAccountScreen = {
                    navController.navigate(SettingsRoute.DeleteAccount)
                }
            )
        }

        composable<SettingsRoute.LocalVerification> {
            LocalVerificationScreenContainer(
                modifier = Modifier.fillMaxSize().background(AconTheme.color.Gray900),
                navigateToSettingsScreen = { navController.popBackStack() },
                navigateToAreaVerification = {
                    navController.navigate(
                        AreaVerificationRoute.AreaVerification(
                            verifiedAreaId = it,
                            route = "settings"
                        )
                    )
                }
            )
        }

        composable<SettingsRoute.DeleteAccount> {
            DeleteAccountScreenContainer(
                modifier = Modifier.fillMaxSize().background(AconTheme.color.Gray900),
                navigateToSettings = {
                    navController.navigateUp()
                },
                navigateToSignIn = {
                    navController.navigate(SignInRoute.SignIn) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}