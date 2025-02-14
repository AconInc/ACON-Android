package com.acon.android.navigation.nested

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.acon.android.domain.repository.SocialRepository
import com.acon.android.feature.areaverification.AreaVerificationRoute
import com.acon.android.feature.signin.screen.SignInRoute
import com.acon.android.feature.signin.screen.SignInScreenContainer
import com.acon.android.feature.spot.SpotRoute

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
                    navController.navigate(AreaVerificationRoute.RequireAreaVerification)
                },
                socialRepository = socialRepository,
            )
        }
    }
}
