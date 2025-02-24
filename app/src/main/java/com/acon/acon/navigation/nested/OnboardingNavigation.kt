package com.acon.acon.navigation.nested

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.acon.acon.feature.SettingsRoute
import com.acon.acon.feature.onboarding.OnboardingRoute
import com.acon.acon.feature.onboarding.screen.OnboardingScreen.composable.OnboardingContainer
import com.acon.acon.feature.onboarding.screen.PrefResultLoadingScreen.composable.PrefResultLoadingScreenContainer
import com.acon.acon.feature.profile.composable.ProfileRoute
import com.acon.acon.feature.spot.SpotRoute


internal fun NavGraphBuilder.onboardingNavigationNavigation(
    navController: NavHostController
) {

    navigation<OnboardingRoute.Graph>(
        startDestination = OnboardingRoute.OnboardingScreen.notfromSettings()
    ) {
        composable<OnboardingRoute.OnboardingScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<OnboardingRoute.OnboardingScreen>()
            val fromSettings = args.fromSettings

            OnboardingContainer(
                navigateToLoadingView = {
                    navController.navigate(OnboardingRoute.LastLoading)
                },
                navigateToSpotListView = {
                    navController.navigate(SpotRoute.SpotList)
                },
                cancelOnboarding = {
                    if (fromSettings) { // 스택에 설정이 있는 경우, 즉 설정에서 온보딩으로 들어온 경우
                        navController.navigate(SettingsRoute.Settings) {
                            popUpTo(SettingsRoute.Settings) {
                                inclusive = false
                            }
                        }
                    } else { // 그 이외, 최초 로그인 후 온보딩으로 들어온 경우에 뒤로가기를 누른 경우
                        navController.popBackStack()
                    }
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