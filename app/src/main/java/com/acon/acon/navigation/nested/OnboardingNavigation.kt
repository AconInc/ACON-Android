package com.acon.acon.navigation.nested

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.acon.acon.feature.onboarding.OnboardingRoute
import com.acon.acon.feature.onboarding.screen.OnboardingScreen.composable.OnboardingContainer
import com.acon.acon.feature.onboarding.screen.PrefResultLoadingScreen.composable.PrefResultLoadingScreenContainer
import com.acon.acon.feature.spot.SpotRoute


internal fun NavGraphBuilder.onboardingNavigationNavigation(
    navController: NavHostController
) {

    navigation<OnboardingRoute.Graph>(
        startDestination = OnboardingRoute.OnboardingScreen
    ) {
        composable<OnboardingRoute.OnboardingScreen> {
            OnboardingContainer(
                navigateToLoadingView = {
                    navController.navigate(OnboardingRoute.LastLoading)
                },
                navigateToSpotListView = {
                    navController.navigate(SpotRoute.SpotList)
                }
            )
        }

        composable<OnboardingRoute.LastLoading> {
            PrefResultLoadingScreenContainer(
                navigateToSpotListView = {
                    navController.navigate(SpotRoute.SpotList)
                }
            )
        }

    }
}