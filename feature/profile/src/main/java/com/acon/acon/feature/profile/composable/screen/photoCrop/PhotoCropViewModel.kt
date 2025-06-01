package com.acon.acon.feature.profile.composable.screen.photoCrop

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PhotoCropViewModel @Inject constructor(

) : ViewModel(), ContainerHost<PhotoCropState, PhotoCropSideEffect> {
    override val container = container<PhotoCropState, PhotoCropSideEffect>(PhotoCropState())

    fun navigateToBack() = intent {
        postSideEffect(PhotoCropSideEffect.NavigateToBack)
    }

    fun navigateToProfileMod(selectedPhotoUri: String) = intent {
        postSideEffect(PhotoCropSideEffect.NavigateToProfileMod(selectedPhotoUri))
    }
}

data class PhotoCropState(
    val selectedPhotoUri: String = ""
)

sealed interface PhotoCropSideEffect {
    data object NavigateToBack : PhotoCropSideEffect
    data class NavigateToProfileMod(val selectedPhotoUri: String) : PhotoCropSideEffect
}