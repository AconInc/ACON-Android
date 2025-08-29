package com.acon.acon.navigation.nested

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.acon.acon.core.designsystem.effect.screenDefault
import com.acon.acon.core.navigation.LocalNavController
import com.acon.acon.core.navigation.route.AreaVerificationRoute
import com.acon.acon.core.navigation.route.OnboardingRoute
import com.acon.acon.core.navigation.route.SettingsRoute
import com.acon.acon.core.navigation.route.SpotRoute
import com.acon.acon.core.navigation.utils.contains
import com.acon.acon.core.navigation.utils.hasPreviousBackStackEntry
import com.acon.acon.core.navigation.utils.navigateAndClear
import com.acon.feature.onboarding.area.composable.AreaVerificationScreenContainer
import com.acon.feature.onboarding.area.composable.VerifyInMapScreenContainer

fun NavGraphBuilder.areaVerificationNavigation(
    navController: NavHostController
) {
    navigation<AreaVerificationRoute.Graph>(
        startDestination = AreaVerificationRoute.AreaVerification
    ) {
        composable<AreaVerificationRoute.AreaVerification> {
            AreaVerificationScreenContainer(
                modifier = Modifier
                    .screenDefault()
                    .statusBarsPadding(),
                onNavigateToVerifyInMap = {
                    navController.navigate(AreaVerificationRoute.VerifyInMap)
                },
                skippable = LocalNavController.current.hasPreviousBackStackEntry().not(),
                onNavigateToChooseDislikes = { navController.navigateAndClear(OnboardingRoute.ChooseDislikes) },
            )
        }

        composable<AreaVerificationRoute.VerifyInMap> {
            VerifyInMapScreenContainer(
                onNavigateToNextScreen = {
                    if (navController.contains<SettingsRoute.UserVerifiedAreas>()) {
                        navController.popBackStack(
                            route = SettingsRoute.UserVerifiedAreas,
                            inclusive = false
                        )
                    }
                    else if (navController.contains<SpotRoute.SpotList>()) {
                        navController.popBackStack(
                            route = SpotRoute.SpotList,
                            inclusive = false
                        )
                    }
                    else {
                        navController.navigateAndClear(OnboardingRoute.ChooseDislikes)
                    }
                },
                onNavigateBack = { navController.popBackStack() },
                modifier = Modifier
                    .screenDefault()
                    .statusBarsPadding()
            )
        }
    }
}