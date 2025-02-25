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
import com.acon.acon.feature.areaverification.AreaVerificationScreenContainer
import com.acon.acon.feature.areaverification.PreferenceMapScreen
import com.acon.acon.feature.onboarding.OnboardingRoute

fun NavGraphBuilder.areaVerificationNavigation(
    navController: NavHostController
) {
    navigation<AreaVerificationRoute.Graph>(
        startDestination = AreaVerificationRoute.RequireAreaVerification()
    ) {
        composable<AreaVerificationRoute.RequireAreaVerification> { backStackEntry ->

            val routeData = backStackEntry.arguments?.let {
                AreaVerificationRoute.RequireAreaVerification(
                    route = it.getString("route"),
                    isEdit = it.getBoolean("isEdit", false)
                )
            }

            AreaVerificationScreenContainer(
                modifier = Modifier.fillMaxSize(),
                route = routeData?.route ?: "onboarding",
                onNewAreaClick = { latitude, longitude ->
                    navController.navigate(
                        AreaVerificationRoute.CheckInMap(
                            latitude = latitude,
                            longitude = longitude,
                            route = routeData?.route,
                            isEdit = routeData?.isEdit ?: false
                        )
                    )
                },
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
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<AreaVerificationRoute.CheckInMap> { backStackEntry ->
            val route = backStackEntry.toRoute<AreaVerificationRoute.CheckInMap>()

            PreferenceMapScreen(
                latitude = route.latitude,
                longitude = route.longitude,
                isEdit = route.isEdit,
                onConfirmClick = {
                    navController.navigate(AreaVerificationRoute.Complete)
                },
                onNavigateToNext = {
                    if (route.route == "settings") {
                        navController.navigate(SettingsRoute.LocalVerification) {
                            popUpTo(AreaVerificationRoute.Graph) {
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