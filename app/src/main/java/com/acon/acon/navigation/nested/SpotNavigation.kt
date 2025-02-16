package com.acon.acon.navigation.nested

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.feature.areaverification.AreaVerificationRoute
import com.acon.acon.feature.spot.SpotRoute
import com.acon.acon.feature.spot.screen.spotdetail.composable.SpotDetailScreenContainer
import com.acon.acon.feature.spot.screen.spotlist.composable.SpotListScreenContainer

internal fun NavGraphBuilder.spotNavigation(
    navController: NavHostController,
    socialRepository: SocialRepository
) {

    navigation<SpotRoute.Graph>(
        startDestination = SpotRoute.SpotList
    ) {
        composable<SpotRoute.SpotList>{
            SpotListScreenContainer(
                socialRepository = socialRepository,
                modifier = Modifier.fillMaxSize(),
                onNavigateToAreaVerification = {
                    navController.navigate(AreaVerificationRoute.RequireAreaVerification) {
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
            SpotDetailScreenContainer(
                onNavigateToSpotListView = {
                    navController.popBackStack()
                },
            )
        }
    }
}