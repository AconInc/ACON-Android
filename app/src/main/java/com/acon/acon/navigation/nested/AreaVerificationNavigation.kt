package com.acon.acon.navigation.nested

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.utils.feature.toast.showToast
import com.acon.acon.feature.SettingsRoute
import com.acon.acon.feature.areaverification.AreaVerificationRoute
import com.acon.acon.feature.areaverification.composable.AreaVerificationScreenContainer
import com.acon.acon.feature.areaverification.composable.PreferenceMapScreen
import com.acon.acon.feature.onboarding.OnboardingRoute

fun NavGraphBuilder.areaVerificationNavigation(
    navController: NavHostController
) {
    navigation<AreaVerificationRoute.Graph>(
        startDestination = AreaVerificationRoute.AreaVerification()
    ) {
        composable<AreaVerificationRoute.AreaVerification> { backStackEntry ->
            val routeData = backStackEntry.toRoute<AreaVerificationRoute.AreaVerification>()

            AreaVerificationScreenContainer(
                modifier = Modifier.fillMaxSize().background(AconTheme.color.Gray900),
                route = routeData.route ?: "onboarding",
                onNextScreen = { latitude, longitude ->
                    navController.navigate(
                        AreaVerificationRoute.CheckInMap(
                            latitude = latitude,
                            longitude = longitude,
                            verifiedAreaId = routeData.verifiedAreaId ?: -1,
                            route = routeData.route
                        )
                    )
                }
            )
        }

        composable<AreaVerificationRoute.CheckInMap> { backStackEntry ->
            val route = backStackEntry.toRoute<AreaVerificationRoute.CheckInMap>()
            val context = LocalContext.current

            PreferenceMapScreen(
                latitude = route.latitude,
                longitude = route.longitude,
                previousVerifiedAreaId = route.verifiedAreaId,
                onNavigateToNext = {
                    if (route.route == "settings") {
                        context.showToast("인증 되었습니다")
                        navController.popBackStack(route = SettingsRoute.LocalVerification, inclusive = false)
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