package com.acon.acon.navigation.nested

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.acon.acon.feature.spot.SpotRoute
import com.acon.acon.feature.upload.UploadRoute
import com.acon.acon.feature.upload.UploadSuccessContainer
import com.acon.acon.feature.upload.v2.composable.search.UploadSearchScreenContainer

internal fun NavGraphBuilder.uploadNavigation(
    navController: NavHostController
) {
    navigation<UploadRoute.Graph>(
        startDestination = UploadRoute.Search
    ) {
        composable<UploadRoute.Search> {
            UploadSearchScreenContainer(
                onNavigateBack = navController::popBackStack,
                onNavigateToReview = {
                    navController.navigate(UploadRoute.Review)
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        composable<UploadRoute.Review> {
            UploadSuccessContainer(
                onNavigateToSpotList = {
                    navController.navigate(SpotRoute.SpotList) {
                        popUpTo(UploadRoute.Graph) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
