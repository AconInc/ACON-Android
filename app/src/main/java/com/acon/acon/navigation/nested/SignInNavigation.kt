package com.acon.acon.navigation.nested

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.acon.acon.feature.areaverification.AreaVerificationRoute
import com.acon.acon.feature.signin.screen.SignInRoute
import com.acon.acon.feature.signin.screen.SignInScreenContainer
import com.acon.acon.feature.spot.SpotRoute

internal fun NavGraphBuilder.signInNavigationNavigation(
    navController: NavHostController,
) {

    navigation<SignInRoute.Graph>(
        startDestination = SignInRoute.SignIn
    ) {
        composable<SignInRoute.SignIn>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            SignInScreenContainer(
                navigateToSpotListView = {
                    navController.navigate(SpotRoute.SpotList) {
                        popUpTo(SignInRoute.Graph) {
                            inclusive = true
                        }
                    }
                },
                navigateToAreaVerification = {
                    navController.navigate(
                        AreaVerificationRoute.AreaVerification("onboarding")
                    ) {
                        popUpTo(SignInRoute.Graph) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}