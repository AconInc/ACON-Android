package com.acon.acon.navigation.nested

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.acon.feature.profile.ProfileRoute
import com.acon.feature.profile.screen.profile.composable.ProfileScreenContainer
import com.acon.feature.profile.screen.profileMod.composable.ProfileModScreenContainer

internal fun NavGraphBuilder.profileNavigation(
    navController: NavHostController
) {

    navigation<ProfileRoute.Graph>(
        startDestination = ProfileRoute.ProfileMod,
        enterTransition = {
            EnterTransition.None
        }, exitTransition = {
            ExitTransition.None
        }
    ) {

        composable<ProfileRoute.Profile> {
            ProfileScreenContainer(
                modifier = Modifier.fillMaxSize()
            )
        }

        composable<ProfileRoute.ProfileMod> {
            ProfileModScreenContainer(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}