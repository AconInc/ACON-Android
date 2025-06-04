package com.acon.acon.feature.profile.composable.screen.galleryList

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
        val bucketCountMap = mutableMapOf<String, Int>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val albumId = cursor.getString(bucketIdColumn)
                val albumName = cursor.getString(bucketNameColumn) ?: "기타"
                val imageId = cursor.getLong(idColumn)
                val coverUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId.toString())

                bucketCountMap[albumId] = bucketCountMap.getOrDefault(albumId, 0) + 1

                if (!albumMap.containsKey(albumId)) {
                    albumMap[albumId] = Album(
                        id = albumId,
                        name = albumName,
                        coverUri = coverUri,
                        imageCount = 0
                    )
                }
            }
        }

        return albumMap.values.map { album ->
            album.copy(imageCount = bucketCountMap[album.id] ?: 0)
        }.toMutableList()
    }

    fun onClickAlbum(albumId: String, albumName: String) = intent {
        postSideEffect(GalleryListSideEffect.NavigateToAlbumGrid(albumId, albumName))
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
    val coverUri: Uri,
    val imageCount: Int
)