package com.acon.acon.navigation.nested

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.navigation.route.AreaVerificationRoute
import com.acon.acon.core.navigation.route.ProfileRoute
import com.acon.acon.core.navigation.route.SpotRoute
import com.acon.acon.feature.spot.screen.spotdetail.composable.SpotDetailScreenContainer
import com.acon.acon.feature.spot.screen.spotlist.composable.SpotListScreenContainer
import com.acon.acon.core.model.model.spot.SpotNavigationParameter
import com.acon.acon.core.navigation.route.UploadRoute
import com.acon.acon.core.navigation.type.spotNavigationParameterNavType

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
            SpotListScreenContainer(
                onNavigateToUploadScreen = {
                    navController.navigate(UploadRoute.Graph)
                },
                onNavigateToProfileScreen = {
                    navController.navigate(ProfileRoute.Graph)
                },
                onNavigateToSpotDetailScreen = { spot, tm ->
                    navController.navigate(
                        SpotRoute.SpotDetail(
                            SpotNavigationParameter(
                                spot.id,
                                spot.tags,
                                tm,
                                spot.eta,
                                null,
                                null
                            )
                        )
                    )
                },
                onNavigateToDeeplinkSpotDetailScreen = { spotNav ->
                    navController.navigate(SpotRoute.SpotDetail(spotNav))
                },
                onNavigateToAreaVerificationScreen = { lat, lon ->
                    navController.navigate(AreaVerificationRoute.CheckInMap(
                        latitude = lat,
                        longitude = lon,
                        verifiedAreaId = -1,
                        route = "spotlist"
                    ))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(AconTheme.color.Gray900)
            )
        }

        composable<SpotRoute.SpotDetail>(
            typeMap = mapOf(spotNavigationParameterNavType)
        ) {
            SpotDetailScreenContainer(
                onNavigateToBack = {
                    navController.popBackStack()
                },
                onBackToAreaVerification = {
                    navController.navigate(
                        AreaVerificationRoute.Graph
                    ) {
                        popUpTo(SpotRoute.Graph) {
                            inclusive = false
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}