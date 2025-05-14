package com.acon.acon.navigation.nested

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.acon.acon.core.designsystem.animation.bottomUpEnterTransition
import com.acon.acon.core.designsystem.animation.topDownExitTransition
import com.acon.acon.core.designsystem.theme.AconTheme
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
        composable<UploadRoute.Search>(
            enterTransition = {
                bottomUpEnterTransition()
            }, exitTransition = {
                topDownExitTransition()
            }
        ) {
            UploadSearchScreenContainer(
                onNavigateBack = navController::popBackStack,
                onNavigateToReview = { spot ->
                    navController.navigate(UploadRoute.Review(spot))
                },
                modifier = Modifier.fillMaxSize().background(AconTheme.color.Gray900)
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
