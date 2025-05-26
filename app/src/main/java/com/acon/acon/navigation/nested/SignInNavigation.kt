package com.acon.acon.navigation.nested

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.feature.areaverification.AreaVerificationRoute
import com.acon.acon.feature.signin.screen.SignInRoute
import com.acon.acon.feature.signin.screen.SignInScreenContainer
import com.acon.acon.feature.spot.SpotRoute

internal fun NavGraphBuilder.signInNavigationNavigation(
    navController: NavHostController,
    socialRepository: SocialRepository
) {

    navigation<SignInRoute.Graph>(
        startDestination = SignInRoute.SignIn,
    ) {
        composable<SignInRoute.SignIn> {
            SignInScreenContainer(
                navigateToSpotListView = {
                    navController.navigate(SpotRoute.SpotList)
                },
                navigateToAreaVerification = {
                    navController.navigate(
                        AreaVerificationRoute.AreaVerificationHome("onboarding")
                    )
                },
                socialRepository = socialRepository,
            )
        }
    }
}
