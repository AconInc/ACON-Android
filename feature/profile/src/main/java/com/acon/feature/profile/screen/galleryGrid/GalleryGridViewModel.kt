package com.acon.feature.profile.screen.galleryGrid

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import com.acon.feature.profile.screen.galleryList.Album
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.io.File
import javax.inject.Inject

@HiltViewModel
class GalleryGridViewModel @Inject constructor(
    private val application: Application
): ViewModel(), ContainerHost<GalleryGridState, GalleryGridSideEffect> {

    override val container = container<GalleryGridState, GalleryGridSideEffect>(GalleryGridState())

    private fun getPhotoList(albumId: String, context: Context): List<Uri> {
        val photoList = mutableListOf<Uri>()

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(albumId)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (cursor.moveToNext()) {
                val photoUri = Uri.fromFile(File(cursor.getString(dataColumn)))
                photoList.add(photoUri)
            }
        }

        return photoList
    }

    fun loadPhotos(albumId: String) = intent {
        val photos = getPhotoList(albumId, application.applicationContext)
        reduce { state.copy(photoList = photos) }
    }

    fun onPhotoSelected(photoUri: Uri) = intent {
        reduce { state.copy(selectedPhotoUri = photoUri) }
    }

    fun confirmSelection(photoUri: Uri) = intent {
        state.selectedPhotoUri?.let {
            //postSideEffect(GalleryGridSideEffect.ReturnToProfileModScreen(photoUri))
            postSideEffect(GalleryGridSideEffect.ReturnToProfileModScreen)
        }
    }
}

data class GalleryGridState(
    val albumList: List<Album> = emptyList(),
    val photoList: List<Uri> = emptyList(),
    val selectedPhotoUri: Uri? = null
)

sealed interface GalleryGridSideEffect {
    //data class ReturnToProfileModScreen(val uri: Uri) : GalleryGridSideEffect
    data object ReturnToProfileModScreen : GalleryGridSideEffect
}




