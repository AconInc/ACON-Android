package com.acon.acon.navigation.nested

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.onboarding.OnboardingRoute
import com.acon.acon.feature.onboarding.screen.composable.ChooseDislikesScreenContainer
import com.acon.acon.feature.spot.SpotRoute


internal fun NavGraphBuilder.onboardingNavigationNavigation(
    navController: NavHostController
) {

    navigation<OnboardingRoute.Graph>(
        startDestination = OnboardingRoute.ChooseDislikes
    ) {
        composable<OnboardingRoute.ChooseDislikes> {
            ChooseDislikesScreenContainer(
                onNavigateToHome = {
                    navController.navigate(SpotRoute.Graph) {
                        popUpTo(OnboardingRoute.Graph) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(AconTheme.color.Gray900)
                    .systemBarsPadding()
            )
        }
    }
}