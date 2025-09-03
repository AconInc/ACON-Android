package com.acon.acon.feature.profile.composable.screen.profileMod.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.feature.profile.composable.screen.profileMod.ProfileModSideEffectLegacy
import com.acon.acon.feature.profile.composable.screen.profileMod.ProfileModViewModelLegacy
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ProfileModScreenContainerLegacy(
    modifier: Modifier = Modifier,
    viewModel: ProfileModViewModelLegacy = hiltViewModel(),
    selectedPhotoId: String? = null,
    onNavigateToBack: () -> Unit = {},
    onClickComplete: () -> Unit = {},
) {
    val state by viewModel.collectAsState()

    LaunchedEffect(selectedPhotoId) {
        if (selectedPhotoId?.isNotEmpty() == true) {
            viewModel.updateProfileImage(selectedPhotoId)
        }
    }

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is ProfileModSideEffectLegacy.NavigateBack -> {
                onNavigateToBack()
            }

            is ProfileModSideEffectLegacy.UpdateProfileImageLegacy -> {
                selectedPhotoId.let {
                    viewModel.updateProfileImage(selectedPhotoId ?: "")
                }
            }

            is ProfileModSideEffectLegacy.NavigateToProfileLegacy -> {
                onClickComplete()
            }
        }
    }

    ProfileModScreenLegacy(
        modifier = modifier,
        state = state,
        navigateToBack = viewModel::navigateToBack,
        onNicknameChanged = viewModel::onNicknameChanged,
        onBirthdayChanged = viewModel::onBirthdayChanged,
        onFocusChanged = viewModel::onFocusChanged,
        onRequestExitDialog = viewModel::onRequestExitDialog,
        onDisMissExitDialog = viewModel::onDisMissExitDialog,
        onRequestProfileEditModal = viewModel::onRequestProfileEditModal,
        onDisMissProfileEditModal = viewModel::onDisMissProfileEditModal,
        onUpdateProfileImage = viewModel::updateProfileImage,
        onClickSaveButton = viewModel::getPreSignedUrl
    )
}