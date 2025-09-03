package com.acon.acon.navigation.nested

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.navigation.route.ProfileRouteLegacy
import com.acon.acon.core.navigation.route.SettingsRoute
import com.acon.acon.core.navigation.route.SpotRoute
import com.acon.acon.core.navigation.route.UploadRoute
import com.acon.acon.feature.profile.composable.screen.bookmark.composable.BookmarkScreenContainer
import com.acon.acon.feature.profile.composable.screen.profile.composable.ProfileScreenContainerLegacy
import com.acon.acon.feature.profile.composable.screen.profileMod.composable.ProfileModScreenContainerLegacy

internal fun NavGraphBuilder.profileNavigationLegacy(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    navigation<ProfileRouteLegacy.Graph>(
        startDestination = ProfileRouteLegacy.ProfileLegacy,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable<ProfileRouteLegacy.ProfileLegacy> {
            ProfileScreenContainerLegacy(
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(AconTheme.color.Gray900)
                    .statusBarsPadding(),
                onNavigateToSpotDetailScreen = {
                    navController.navigate(
                        SpotRoute.SpotDetail(
                            com.acon.acon.core.model.model.spot.SpotNavigationParameter(
                                it,
                                emptyList(),
                                null,
                                null,
                                null,
                                true
                            )
                        )
                    )
                },
                onNavigateToBookMarkScreen = {
                    navController.navigate(ProfileRouteLegacy.Bookmark)
                },
                onNavigateToSpotListScreen = {
                    navController.popBackStack(
                        route = SpotRoute.SpotList,
                        inclusive = false
                    )
                },
                onNavigateToSettingsScreen = { navController.navigate(SettingsRoute.Settings) },
                onNavigateToProfileEditScreen = {
                    navController.navigate(
                        ProfileRouteLegacy.ProfileModLegacy(
                            null
                        )
                    )
                },
                onNavigateToUploadScreen = {
                    navController.navigate(UploadRoute.Graph)
                }
            )
        }

        composable<ProfileRouteLegacy.ProfileModLegacy> { backStackEntry ->
            val savedStateHandle = backStackEntry.savedStateHandle
            val selectedPhotoId by savedStateHandle
                .getStateFlow<String?>("selectedPhotoId", null)
                .collectAsState()

            ProfileModScreenContainerLegacy(
                modifier = Modifier.fillMaxSize(),
                selectedPhotoId = selectedPhotoId,
                onNavigateToBack = {
                    navController.popBackStack()
                },
                onClickComplete = {
                    navController.popBackStack()
                },
            )
        }

        composable<ProfileRouteLegacy.Bookmark> {
            BookmarkScreenContainer(
                modifier = Modifier.fillMaxSize(),
                onNavigateToBack = {
                    navController.popBackStack()
                },
                onNavigateToSpotDetailScreen = {
                    navController.navigate(
                        SpotRoute.SpotDetail(
                            com.acon.acon.core.model.model.spot.SpotNavigationParameter(
                                it,
                                emptyList(),
                                null,
                                null,
                                null,
                                true
                            )
                        )
                    )
                },
            )
        }
    }
}