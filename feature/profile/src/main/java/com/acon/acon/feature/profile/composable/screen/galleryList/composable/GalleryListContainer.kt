package com.acon.acon.feature.profile.composable.screen.galleryList.composable

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.feature.profile.composable.screen.galleryList.GalleryListSideEffect
import com.acon.acon.feature.profile.composable.screen.galleryList.GalleryListViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun GalleryListContainer(
    modifier: Modifier = Modifier,
    viewModel: GalleryListViewModel = hiltViewModel(),
    onAlbumSelected: (String, String) -> Unit = { _, _ -> },
    onBackClicked: () -> Unit = {},
) {
    val state by viewModel.collectAsState()

    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.updateStorageAccess()
    }

    viewModel.collectSideEffect {
        when (it) {
            is GalleryListSideEffect.NavigateToSettings -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", it.packageName, null)
                }
                settingsLauncher.launch(intent)
            }

            is GalleryListSideEffect.NavigateToAlbumGrid -> {
                onAlbumSelected(it.albumId, it.albumName)
            }
        }
    }

    GalleryListScreen(
        state = state,
        modifier = modifier,
        onBackClicked = onBackClicked,
        onRefreshAlbum = viewModel::updateAlbums,
        onClickPermissionSettings = viewModel::onPermissionSettingClick,
        requestMediaPermission = viewModel::requestMediaPermission,
        resetMediaPermission = viewModel::resetMediaPermission,
        requestMediaPermissionModal = viewModel::requestMediaPermissionModal,
        dismissMediaPermissionModal = viewModel::dismissMediaPermissionModal,
        requestMediaPermissionDialog = viewModel::requestMediaPermissionDialog,
        dismissMediaPermissionDialog = viewModel::dismissMediaPermissionDialog,
        onAlbumSelected = viewModel::onClickAlbum
    )
}