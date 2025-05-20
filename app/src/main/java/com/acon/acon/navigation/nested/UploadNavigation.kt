package com.acon.acon.navigation.nested

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.acon.acon.core.designsystem.animation.bottomUpEnterTransition
import com.acon.acon.core.designsystem.animation.topDownExitTransition
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.upload.UploadRoute
import com.acon.acon.feature.upload.screen.composable.complete.UploadCompleteScreenContainer
import com.acon.acon.feature.upload.screen.composable.review.UploadReviewScreenContainer
import com.acon.acon.feature.upload.screen.composable.search.UploadSearchScreenContainer
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
                ExitTransition.None
            }, popEnterTransition = {
                EnterTransition.None
            }, popExitTransition = {
                topDownExitTransition()
            }
        ) {

            UploadSearchScreenContainer(
                onNavigateBack = navController::popBackStack,
                onNavigateToReview = { spot ->
                    navController.navigate(UploadRoute.Review(spot))
                },
                modifier = Modifier
                    .background(AconTheme.color.Gray900)
                    .systemBarsPadding()
                    .fillMaxSize()
            )
        }

        composable<UploadRoute.Review>(
            typeMap = mapOf(searchedSpotNavType)
        ) {
            UploadReviewScreenContainer(
                onNavigateBack = navController::popBackStack,
                onComplete = {
                    navController.navigate(UploadRoute.Complete(it)) {
                        popUpTo(UploadRoute.Search) {
                            inclusive = false
                        }
                    }
                },
                modifier = Modifier
                    .background(AconTheme.color.Gray9)
                    .systemBarsPadding()
                    .fillMaxSize(),
            )
        }

        composable<UploadRoute.Complete>(
            exitTransition = {
                ExitTransition.None
            }
        ) {
            UploadCompleteScreenContainer(
                spotName = it.toRoute<UploadRoute.Complete>().spotName,
                onNavigateToHome = {
                    navController.popBackStack(UploadRoute.Search, true)
                },
                modifier = Modifier
                    .background(AconTheme.color.Gray9)
                    .systemBarsPadding()
                    .fillMaxSize()
            )
        }
    }
}
