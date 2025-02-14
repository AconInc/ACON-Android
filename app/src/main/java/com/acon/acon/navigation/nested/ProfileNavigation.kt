package com.acon.acon.navigation.nested

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.acon.feature.areaverification.AreaVerificationRoute
import com.acon.feature.profile.ProfileRoute
import com.acon.feature.profile.screen.galleryGrid.composable.GalleryGridContainer
import com.acon.feature.profile.screen.galleryList.composable.GalleryListContainer
import com.acon.feature.profile.screen.photoCrop.composable.PhotoCropContainer
import com.acon.feature.profile.screen.profile.composable.ProfileScreenContainer
import com.acon.feature.profile.screen.profileMod.composable.ProfileModScreenContainer

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal fun NavGraphBuilder.profileNavigation(
    navController: NavHostController
) {

    navigation<ProfileRoute.Graph>(
        startDestination = ProfileRoute.ProfileMod,
        enterTransition = {
            EnterTransition.None
        }, exitTransition = {
            ExitTransition.None
        }
    ) {

        composable<ProfileRoute.Profile> {
            ProfileScreenContainer(
                modifier = Modifier.fillMaxSize()
            )
        }

        composable<ProfileRoute.ProfileMod> { backStackEntry ->
//            val route = backStackEntry.toRoute<ProfileRoute.ProfileMod>()
//            val selectedPhotoUri = route.photoUri?.let { Uri.parse(it) }

            ProfileModScreenContainer(
                modifier = Modifier.fillMaxSize(),
                //selectedPhotoUri = selectedPhotoUri.toString(),
                selectedPhotoUri = "",
                onNavigateToProfile = {
                    navController.navigate(ProfileRoute.Profile)
                },
                onNavigateToAreaVerification = {
                    navController.navigate(AreaVerificationRoute.RequireAreaVerification)
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
                onCompleteSelected = { photoId : String ->
//                    navController.navigate(ProfileRoute.ProfileMod(photoId)) // 얘가 String형 인자 받도록 수정해야 함.
                }
            )
        }
    }
}