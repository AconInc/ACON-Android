package com.acon.acon.navigation.nested

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.acon.domain.repository.SocialRepository
import com.acon.feature.SettingsRoute
import com.acon.feature.areaverification.AreaVerificationRoute
import com.acon.feature.profile.ProfileRoute
import com.acon.feature.profile.screen.profile.composable.screen.composable.ProfileScreenContainer

internal fun NavGraphBuilder.profileNavigation(
    navController: NavHostController,
    socialRepository: SocialRepository
) {

    navigation<ProfileRoute.Graph>(
        startDestination = ProfileRoute.Profile,
        enterTransition = {
            EnterTransition.None
        }, exitTransition = {
            ExitTransition.None
        }
    ) {

        composable<ProfileRoute.Profile> {
            ProfileScreenContainer(
                socialRepository = socialRepository,
                modifier = Modifier.fillMaxSize(),
                onNavigateToSettingsScreen = { navController.navigate(SettingsRoute.Settings) },
                onNavigateToProfileEditScreen = {}, // TODO - 지원이꺼 합치면 추가
                onNavigateToAreaVerificationScreen = {
                    navController.navigate(AreaVerificationRoute.RequireAreaVerification) {
                        popUpTo(ProfileRoute.Graph) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}