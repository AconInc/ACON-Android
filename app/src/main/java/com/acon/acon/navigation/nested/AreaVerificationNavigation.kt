package com.acon.acon.navigation.nested

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.acon.acon.feature.SettingsRoute
import com.acon.acon.feature.areaverification.AreaVerificationRoute
import com.acon.acon.feature.areaverification.composable.AreaVerificationScreenContainer
import com.acon.acon.feature.areaverification.composable.PreferenceMapScreen
import com.acon.acon.feature.onboarding.OnboardingRoute

fun NavGraphBuilder.areaVerificationNavigation(
    navController: NavHostController
) {
    navigation<AreaVerificationRoute.Graph>(
        startDestination = AreaVerificationRoute.AreaVerificationHome()
    ) {
        composable<AreaVerificationRoute.AreaVerificationHome> { backStackEntry ->
            val routeData = backStackEntry.arguments?.let {
                AreaVerificationRoute.AreaVerificationHome(
                    route = it.getString("route"),
                    isEdit = it.getBoolean("isEdit", false)
                )
            }

            AreaVerificationScreenContainer(
                modifier = Modifier.fillMaxSize(),
                route = routeData?.route ?: "onboarding",
                onNextScreen = { latitude, longitude ->
                    navController.navigate(
                        AreaVerificationRoute.CheckInMap(
                            latitude = latitude,
                            longitude = longitude,
                            route = routeData?.route,
                            isEdit = routeData?.isEdit ?: false
                        )
                    )
                },
            )
        }

        composable<AreaVerificationRoute.CheckInMap> { backStackEntry ->
            val route = backStackEntry.toRoute<AreaVerificationRoute.CheckInMap>()

            PreferenceMapScreen(
                latitude = route.latitude,
                longitude = route.longitude,
                isEdit = route.isEdit,
                onNavigateToNext = {
                    if (route.route == "settings") {
                        navController.navigate(SettingsRoute.LocalVerification) {
                            popUpTo(SettingsRoute.LocalVerification) {
                                inclusive = true
                            }
                        }
                    } else {
                        navController.navigate(OnboardingRoute.Graph) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}