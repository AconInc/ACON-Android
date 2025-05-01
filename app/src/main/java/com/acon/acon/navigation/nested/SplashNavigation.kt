package com.acon.acon.navigation.nested

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.acon.acon.feature.signin.screen.SignInRoute
import com.acon.acon.feature.signin.splash.SplashScreenContainer
import com.acon.acon.feature.spot.SpotRoute
import com.acon.acon.navigation.route.SplashRoute

internal fun NavGraphBuilder.splashNavigationNavigation(
    navController: NavHostController
) {
    navigation<SplashRoute.Graph>(
        startDestination = SplashRoute.Splash
    ) {
        composable<SplashRoute.Splash> {
            SplashScreenContainer(
                navigationToSignIn = {
                    navController.navigate(SignInRoute.Graph) {
                        popUpTo(SignInRoute.Graph) {
                            inclusive = true
                        }
                    }
                },
                navigationToSpotListView = {
                    navController.navigate(SpotRoute.Graph) {
                        popUpTo(SpotRoute.Graph) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
