package com.acon.acon.navigation.nested

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.acon.acon.feature.upload.UploadRoute
import com.acon.acon.feature.upload.v2.composable.review.UploadReviewScreenContainer
import com.acon.acon.feature.upload.v2.composable.search.UploadSearchScreenContainer
import com.acon.feature.common.navigation.searchedSpotNavType

internal fun NavGraphBuilder.uploadNavigation(
    navController: NavHostController
) {
    navigation<UploadRoute.Graph>(
        startDestination = UploadRoute.Search
    ) {
        composable<UploadRoute.Search> {
            UploadSearchScreenContainer(
                onNavigateBack = navController::popBackStack,
                onNavigateToReview = { spot ->
                    navController.navigate(UploadRoute.Review(spot))
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        composable<UploadRoute.Review>(
            typeMap = mapOf(searchedSpotNavType)
        ) {
            UploadReviewScreenContainer(
                onNavigateBack = navController::popBackStack,
                onComplete = {
                    // TODO("업로드 완료 화면으로 이동")
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
