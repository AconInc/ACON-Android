package com.acon.feature.profile.screen.galleryList

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class GalleryListViewModel @Inject constructor(
    private val application: Application
) : ViewModel(), ContainerHost<GalleryListState, GalleryListSideEffect> {

    override val container = container<GalleryListState, GalleryListSideEffect>(GalleryListState())

    private fun getAlbumList(context: Context): List<Album> {
        val albumMap = mutableMapOf<String, Album>()

        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID
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
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val albumName = cursor.getString(bucketColumn) ?: "기타"
                val albumId = cursor.getString(bucketIdColumn)

                val imageId = cursor.getLong(idColumn)
                val coverUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId.toString())

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

}

data class GalleryListState(
    val albumList: List<Album> = mutableListOf()
)

sealed interface GalleryListSideEffect {
    data class NavigateToAlbumGrid(val albumId: String, val albumName: String) : GalleryListSideEffect
}

data class Album(
    val id: String,
    val name: String,
    val coverUri: Uri
)