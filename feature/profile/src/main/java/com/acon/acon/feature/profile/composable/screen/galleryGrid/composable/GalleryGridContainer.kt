package com.acon.acon.feature.profile.composable.screen.galleryGrid.composable

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.feature.profile.composable.screen.galleryGrid.GalleryGridSideEffect
import com.acon.acon.feature.profile.composable.screen.galleryGrid.GalleryGridViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun GalleryGridContainer(
    modifier: Modifier = Modifier,
    viewModel: GalleryGridViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {},
    onNavigateToPhotoCrop: (String) -> Unit = {}
) {
    val state by viewModel.collectAsState()

    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.updateStorageAccess()
    }

    BackHandler {
        onBackClicked()
    }

    viewModel.collectSideEffect {
        when (it) {
            is GalleryGridSideEffect.NavigateToSettings -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", it.packageName, null)
                }
                settingsLauncher.launch(intent)
            }

            is GalleryGridSideEffect.NavigateToPhotoCropScreen -> {
                onNavigateToPhotoCrop(it.photoUri)
            }
        }
    }

    GalleryGridScreen(
        state = state,
        albumName = viewModel.albumName,
        modifier = modifier,
        onBackClicked = onBackClicked,
        onLoadMoreImage = viewModel::loadNextPage,
        onUpdateAllImages = viewModel::updateAllImages,
        onUpdateUserSelectedImages = viewModel::updateUserSelectedImages,
        onClickPermissionSettings = viewModel::onPermissionSettingClick,
        requestMediaPermission = viewModel::requestMediaPermission,
        resetMediaPermission = viewModel::resetMediaPermission,
        requestMediaPermissionModal = viewModel::requestMediaPermissionModal,
        dismissMediaPermissionModal = viewModel::dismissMediaPermissionModal,
        requestMediaPermissionDialog = viewModel::requestMediaPermissionDialog,
        dismissMediaPermissionDialog = viewModel::dismissMediaPermissionDialog,
        onPhotoSelected = viewModel::onPhotoSelected,
        onConfirmSelected = viewModel::onConfirmSelected
    )
}