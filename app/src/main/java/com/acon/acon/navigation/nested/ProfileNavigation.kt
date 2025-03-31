package com.acon.acon.navigation.nested

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.acon.acon.feature.profile.composable.screen.galleryGrid.composable.GalleryGridContainer
import com.acon.acon.feature.profile.composable.screen.galleryList.composable.GalleryListContainer
import com.acon.acon.feature.profile.composable.screen.photoCrop.composable.PhotoCropContainer
import com.acon.acon.feature.profile.composable.screen.profile.composable.ProfileScreenContainer
import com.acon.acon.feature.profile.composable.screen.profileMod.composable.ProfileModScreenContainer
import com.acon.acon.feature.spot.SpotRoute

internal fun NavGraphBuilder.profileNavigation(
    navController: NavHostController,
    socialRepository: SocialRepository
) {

    navigation<ProfileRoute.Graph>(
        startDestination = ProfileRoute.Profile,
        enterTransition = {
            EnterTransition.None
        }, exitTransition = {
            ExitTransition.None
        }
    ) {
        composable<ProfileRoute.Profile> {
            val profileUpdateResult = navController.currentBackStackEntry
                ?.savedStateHandle
                ?.getLiveData<String>("profileUpdateResult")?.value ?: ""

            ProfileScreenContainer(
                socialRepository = socialRepository,
                profileUpdateResult = profileUpdateResult,
                modifier = Modifier.fillMaxSize(),
                onNavigateToSpotListScreen = {
                    navController.navigate(SpotRoute.SpotList) {
                        popUpTo(ProfileRoute.Graph) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToSettingsScreen = { navController.navigate(SettingsRoute.Settings) },
                onNavigateToProfileEditScreen = { navController.navigate(ProfileRoute.ProfileMod.applyDefault()) },
                onNavigateToAreaVerificationScreen = {
                    navController.navigate(AreaVerificationRoute.RequireAreaVerification("onboarding")) {
                        popUpTo(ProfileRoute.Graph) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<ProfileRoute.ProfileMod> { backStackEntry ->
            val savedStateHandle = backStackEntry.savedStateHandle
            val selectedPhotoId = remember { mutableStateOf<String?>(null) }

            LaunchedEffect(Unit) {
                savedStateHandle.getLiveData<String>("selectedPhotoId").observeForever { result ->
                    selectedPhotoId.value = result
                }
            }

            ProfileModScreenContainer(
                modifier = Modifier.fillMaxSize(),
                selectedPhotoId = selectedPhotoId.value.toString(),
                backToProfile = {
                    navController.popBackStack()
                },
                onNavigateToProfile = { profileUpdateResult ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("profileUpdateResult", profileUpdateResult.name)
                    navController.popBackStack()
                },
                onNavigateToCustomGallery = {
                    navController.navigate(ProfileRoute.GalleryList)
                }
            )
        }

        composable<ProfileRoute.GalleryList> {
            GalleryListContainer(
                modifier = Modifier.fillMaxSize(),
                onAlbumSelected = { albumId, albumName ->
                    navController.popBackStack()
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
                onConfirmSelected = { photoId ->
                    navController.popBackStack()
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

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selectedPhotoId", photoId)

                    navController.popBackStack()
                }
            )
        }
    }
}