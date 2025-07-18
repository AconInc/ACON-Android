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
import com.acon.acon.domain.model.spot.SpotNavigationParameter
import com.acon.acon.feature.SettingsRoute
import com.acon.acon.feature.profile.composable.ProfileRoute
import com.acon.acon.feature.profile.composable.screen.bookmark.composable.BookmarkScreenContainer
import com.acon.acon.feature.profile.composable.screen.profile.composable.ProfileScreenContainer
import com.acon.acon.feature.profile.composable.screen.profileMod.composable.ProfileModScreenContainer
import com.acon.acon.feature.spot.SpotRoute
import com.acon.acon.feature.upload.UploadRoute

internal fun NavGraphBuilder.profileNavigation(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    navigation<ProfileRoute.Graph>(
        startDestination = ProfileRoute.Profile,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable<ProfileRoute.Profile> {
            ProfileScreenContainer(
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(AconTheme.color.Gray900)
                    .statusBarsPadding(),
                onNavigateToSpotDetailScreen = {
                    navController.navigate(
                        SpotRoute.SpotDetail(
                            SpotNavigationParameter(it, emptyList(), null, null, null, true)
                        )
                    )
                },
                onNavigateToBookMarkScreen = {
                    navController.navigate(ProfileRoute.Bookmark)
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
                        ProfileRoute.ProfileMod(
                            null
                        )
                    )
                },
                onNavigateToUploadScreen = {
                    navController.navigate(UploadRoute.Graph)
                }
            )
        }

        composable<ProfileRoute.ProfileMod> { backStackEntry ->
            val savedStateHandle = backStackEntry.savedStateHandle
            val selectedPhotoId by savedStateHandle
                .getStateFlow<String?>("selectedPhotoId", null)
                .collectAsState()

            ProfileModScreenContainer(
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

        composable<ProfileRoute.Bookmark> {
            BookmarkScreenContainer(
                modifier = Modifier.fillMaxSize(),
                onNavigateToBack = {
                    navController.popBackStack()
                },
                onNavigateToSpotDetailScreen = {
                    navController.navigate(
                        SpotRoute.SpotDetail(
                            SpotNavigationParameter(it, emptyList(), null, null, null, true)
                        )
                    )
                },
            )
        }
    }
}