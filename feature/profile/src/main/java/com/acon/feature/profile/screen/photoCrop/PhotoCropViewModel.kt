package com.acon.feature.profile.screen.photoCrop

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PhotoCropViewModel @Inject constructor(
    private val application: Application
) : ViewModel(), ContainerHost<PhotoCropState, PhotoCropSideEffect> {

    override val container = container<PhotoCropState, PhotoCropSideEffect>(PhotoCropState())


}

data class PhotoCropState(
    val selectedPhotoUri: String = ""
)

sealed interface PhotoCropSideEffect {
    data class NavigateToProfileMod(val selectedPhotoUri: String) : PhotoCropSideEffect
}
