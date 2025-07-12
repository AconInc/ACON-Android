package com.acon.acon.feature.profile.composable.screen.profileMod.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.feature.profile.composable.screen.profileMod.ProfileModSideEffect
import com.acon.acon.feature.profile.composable.screen.profileMod.ProfileModViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ProfileModScreenContainer(
    modifier: Modifier = Modifier,
    viewModel: ProfileModViewModel = hiltViewModel(),
    selectedPhotoId: String? = null,
    onNavigateToBack: () -> Unit = {},
    onClickComplete: () -> Unit = {}
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
            is ProfileModSideEffect.NavigateBack -> {
                onNavigateToBack()
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