package com.acon.acon.feature.profile.composable.screen.profileMod

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ProfileModScreenContainer(
    modifier: Modifier = Modifier,
    viewModel: ProfileModViewModel = hiltViewModel(),
    selectedPhotoId: String? = null,
    onNavigateToBack: () -> Unit = {},
    onClickComplete: () -> Unit = {},
    onNavigateToCustomGallery: () -> Unit = {}
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(selectedPhotoId) {
        if (selectedPhotoId?.isNotEmpty() == true) {
            viewModel.updateProfileImage(selectedPhotoId)
        }
    }

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is ProfileModSideEffect.NavigateToSettings -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", effect.packageName, null)
                }
                context.startActivity(intent)
            }

            is ProfileModSideEffect.NavigateBack -> {
                onNavigateToBack()
            }

            is ProfileModSideEffect.NavigateToCustomGallery -> {
                onNavigateToCustomGallery()
            }

            is ProfileModSideEffect.UpdateProfileImage -> {
                selectedPhotoId.let {
                    viewModel.updateProfileImage(selectedPhotoId ?: "")
                }
            }

            is ProfileModSideEffect.NavigateToProfile -> {
                onClickComplete()
            }
        }
    }

    ProfileModScreen(
        modifier = modifier,
        state = state,
        navigateToBack = viewModel::navigateToBack,
        navigateToCustomGallery = viewModel::navigateToCustomGallery,
        onNicknameChanged = viewModel::onNicknameChanged,
        onBirthdayChanged = viewModel::onBirthdayChanged,
        onFocusChanged = viewModel::onFocusChanged,
        onRequestExitDialog = viewModel::onRequestExitDialog,
        onDisMissExitDialog = viewModel::onDisMissExitDialog,
        onRequestPhotoPermission = viewModel::onRequestPhotoPermission,
        onDisMissPhotoPermission = viewModel::onDisMissPhotoPermission,
        onRequestPermissionDialog = viewModel::onRequestPermissionDialog,
        onDisMissPermissionDialog = viewModel::onDisMissPermissionDialog,
        moveToSettings = viewModel::onPermissionSettingClick,
        onProfileClicked = viewModel::onRequestProfileEditModal,
        onDisMissProfileEditModal = viewModel::onDisMissProfileEditModal,
        onUpdateProfileImage = viewModel::updateProfileImage,
        onClickSaveButton = viewModel::getPreSignedUrl
    )
}