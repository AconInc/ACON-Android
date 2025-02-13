package com.acon.feature.profile.screen.customGallery

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
class CustomGalleryViewModel @Inject constructor(
    private val application: Application
) : ViewModel(), ContainerHost<CustomGalleryState, CustomGallerySideEffect> {

    override val container = container<CustomGalleryState, CustomGallerySideEffect>(CustomGalleryState())

    private fun getAlbumList(context: Context): List<Album> {
        val albumMap = mutableMapOf<String, Album>()

        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DATA
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val bucketColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (cursor.moveToNext()) {
                val albumName = cursor.getString(bucketColumn) ?: "기타"
                val albumId = cursor.getString(bucketIdColumn)
                val coverUri = Uri.fromFile(File(cursor.getString(dataColumn)))

                if (!albumMap.containsKey(albumId)) {
                    albumMap[albumId] = Album(albumId, albumName, coverUri)
                }
            }
        }

        return albumMap.values.toMutableList()
    }


    fun loadAlbums() = intent {
        val albums = getAlbumList(application.applicationContext)
        reduce { state.copy(albumList = albums) }
    }

    fun onAlbumSelected(albumId: String) = intent {
        postSideEffect(CustomGallerySideEffect.NavigateToAlbumGrid(albumId))
    }


}

data class CustomGalleryState(
    val albumList: List<Album> = mutableListOf()
)

sealed interface CustomGallerySideEffect {
    data class NavigateToAlbumGrid(val albumId: String) : CustomGallerySideEffect
}

data class Album(
    val id: String,
    val name: String,
    val coverUri: Uri
)