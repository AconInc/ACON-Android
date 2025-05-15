package com.acon.acon.navigation.nested

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.feature.areaverification.AreaVerificationRoute
import com.acon.acon.feature.spot.SpotRoute
import com.acon.acon.feature.spot.screen.spotdetail.composable.SpotDetailScreenContainerV2
import com.acon.acon.feature.spot.screen.spotlist.composable.SpotListScreenContainer

internal fun NavGraphBuilder.spotNavigation(
    navController: NavHostController,
    socialRepository: SocialRepository
) {

    navigation<SpotRoute.Graph>(
        startDestination = SpotRoute.SpotList
    ) {
        composable<SpotRoute.SpotList>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ){
            SpotListScreenContainer(
                socialRepository = socialRepository,
                modifier = Modifier.fillMaxSize(),
                onNavigateToAreaVerification = {
                    navController.navigate(AreaVerificationRoute.RequireAreaVerification("onboarding")) {
                        popUpTo(SpotRoute.Graph) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToSpotDetailScreen = {
                    navController.navigate(SpotRoute.SpotDetail(it))
                }
            )
        }

        composable<SpotRoute.SpotDetail> {
            SpotDetailScreenContainerV2(
                modifier = Modifier.fillMaxSize(),
                onNavigateToBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}