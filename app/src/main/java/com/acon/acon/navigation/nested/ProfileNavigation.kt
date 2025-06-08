package com.acon.acon.navigation.nested

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.acon.acon.domain.repository.SocialRepository
import com.acon.acon.feature.SettingsRoute
import com.acon.acon.feature.areaverification.AreaVerificationRoute
import com.acon.acon.feature.profile.composable.ProfileRoute
import com.acon.acon.feature.profile.composable.screen.bookmark.composable.BookmarkScreenContainer
import com.acon.acon.feature.profile.composable.screen.galleryGrid.composable.GalleryGridContainer
import com.acon.acon.feature.profile.composable.screen.galleryList.composable.GalleryListContainer
import com.acon.acon.feature.profile.composable.screen.photoCrop.composable.PhotoCropContainer
import com.acon.acon.feature.profile.composable.screen.profile.composable.ProfileScreenContainer
import com.acon.acon.feature.profile.composable.screen.profileMod.composable.ProfileModScreenContainer
import com.acon.acon.feature.spot.SpotRoute

internal fun NavGraphBuilder.profileNavigation(
    navController: NavHostController,
    socialRepository: SocialRepository,
    snackbarHostState: SnackbarHostState
) {
    navigation<ProfileRoute.Graph>(
        startDestination = ProfileRoute.Profile,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable<ProfileRoute.Profile> {
            ProfileScreenContainer(
                socialRepository = socialRepository,
                snackbarHostState = snackbarHostState,
                modifier = Modifier.fillMaxSize(),
                onNavigateToBookMark = {
                    navController.navigate(ProfileRoute.Bookmark)
                },
                onNavigateToSpotListScreen = {
                    navController.navigate(SpotRoute.SpotList) {
                        popUpTo(ProfileRoute.Graph) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToSettingsScreen = { navController.navigate(SettingsRoute.Settings) },
                onNavigateToProfileEditScreen = {
                    navController.navigate(
                        ProfileRoute.ProfileMod(
                            null
                        )
                    )
                },
                onNavigateToAreaVerificationScreen = {
                    navController.navigate(AreaVerificationRoute.AreaVerification("onboarding")) {
                        popUpTo(ProfileRoute.Graph) {
                            inclusive = true
                        }
                    }
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
                onNavigateToCustomGallery = {
                    navController.navigate(ProfileRoute.GalleryList)
                }
            )
        }

        composable<ProfileRoute.Bookmark> {
            BookmarkScreenContainer(
                modifier = Modifier.fillMaxSize(),
                onNavigateToBack = {
                    navController.popBackStack()
                },
                onNavigateToSpotDetailScreen = {
                    // TODO - 장소상세로 이동
                }
            )
        }

        composable<ProfileRoute.GalleryList> {
            GalleryListContainer(
                modifier = Modifier.fillMaxSize(),
                onAlbumSelected = { albumId, albumName ->
                    navController.navigate(ProfileRoute.GalleryGrid(albumId, albumName))
                },
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }

        composable<ProfileRoute.GalleryGrid> { backStackEntry ->
            val route = backStackEntry.toRoute<ProfileRoute.GalleryGrid>()

            GalleryGridContainer(
                modifier = Modifier.fillMaxSize(),
                albumId = route.albumId,
                albumName = route.albumName,
                onBackClicked = {
                    navController.popBackStack()
                },
                onNavigateToPhotoCrop = { photoId ->
                    navController.navigate(ProfileRoute.PhotoCrop(photoId))
                }
            )
        }

        composable<ProfileRoute.PhotoCrop> { backStackEntry ->
            val route = backStackEntry.toRoute<ProfileRoute.PhotoCrop>()

            PhotoCropContainer(
                modifier = Modifier.fillMaxSize(),
                photoId = route.photoId,
                onCloseClicked = {
                    navController.popBackStack()
                },
                onCompleteSelected = { photoId: String ->
                    navController.getBackStackEntry(ProfileRoute.ProfileMod(null))
                        .savedStateHandle["selectedPhotoId"] = photoId

                    navController.popBackStack(ProfileRoute.ProfileMod(null), false)
                }
            )
        }
    }
}