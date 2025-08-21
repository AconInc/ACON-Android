package com.acon.acon.navigation.nested

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.acon.acon.core.designsystem.effect.screenDefault
import com.acon.acon.core.navigation.LocalNavController
import com.acon.acon.core.navigation.route.OnboardingRoute
import com.acon.acon.core.navigation.route.SettingsRoute
import com.acon.acon.core.navigation.route.SpotRoute
import com.acon.acon.core.navigation.utils.navigateAndClear
import com.acon.feature.onboarding.dislikes.composable.ChooseDislikesScreenContainer
import com.acon.feature.onboarding.introduce.composable.IntroduceScreenContainer


internal fun NavGraphBuilder.onboardingNavigationNavigation(
    navController: NavHostController
) {

    navigation<OnboardingRoute.Graph>(
        startDestination = OnboardingRoute.Introduce
    ) {
        composable<OnboardingRoute.ChooseDislikes> {
            val fromSetting = LocalNavController.current.previousBackStackEntry?.destination?.hasRoute(
                SettingsRoute.Settings::class) ?: false

            ChooseDislikesScreenContainer(
                onNavigateToHome = {
                    navController.navigate(SpotRoute.Graph) {
                        popUpTo(OnboardingRoute.Graph) {
                            inclusive = true
                        }
                    }
                },
                fromSetting = fromSetting,
                modifier = Modifier.screenDefault().systemBarsPadding()
            )
        }

        composable<OnboardingRoute.Introduce> {

            IntroduceScreenContainer(
                modifier = Modifier.screenDefault().statusBarsPadding(),
                onNavigateToHome = {
                    navController.navigateAndClear(SpotRoute.Graph)
                }
            )
        }
    }
}