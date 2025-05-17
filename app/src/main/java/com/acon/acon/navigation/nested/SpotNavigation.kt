package com.acon.acon.navigation.nested

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.acon.acon.feature.spot.SpotRoute
import com.acon.acon.feature.spot.screen.spotdetail.composable.SpotDetailScreenContainer
import com.acon.acon.feature.spot.screen.spotlist.composable.SpotListScreenContainer
import com.acon.feature.common.intent.openMapNavigation

internal fun NavGraphBuilder.spotNavigation(
    navController: NavHostController
) {

    navigation<SpotRoute.Graph>(
        startDestination = SpotRoute.SpotList
    ) {
        composable<SpotRoute.SpotList>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            val context = LocalContext.current
            SpotListScreenContainer(
                onNavigateToSpotDetailScreen = {
                    navController.navigate(SpotRoute.SpotDetail(it.id))
                },
                onNavigateToExternalMap = context::openMapNavigation,
                modifier = Modifier.fillMaxSize()
            )
        }

        composable<SpotRoute.SpotDetail> {
            SpotDetailScreenContainer(
                modifier = Modifier.fillMaxSize(),
                onNavigateToBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}