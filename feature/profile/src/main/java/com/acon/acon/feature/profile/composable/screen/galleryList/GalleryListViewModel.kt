package com.acon.acon.feature.profile.composable.screen.galleryList

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.Immutable
import com.acon.acon.core.ui.permission.media.StorageAccess
import com.acon.acon.core.ui.permission.media.getStorageAccess
import com.acon.acon.core.ui.base.BaseContainerHost
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class GalleryListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : BaseContainerHost<GalleryListUiState, GalleryListSideEffect>() {

    override val container =
        container<GalleryListUiState, GalleryListSideEffect>(GalleryListUiState.Loading) {
            updateStorageAccess()
        }

    fun updateStorageAccess() = intent {
        when (getStorageAccess(context)) {
            StorageAccess.GRANTED -> {
                updateAllAlbums()
            }

            StorageAccess.Partial -> {
                reduce { GalleryListUiState.Partial(albumList = getAlbumList(context.contentResolver)) }
            }

            StorageAccess.Denied -> reduce { GalleryListUiState.Denied() }
        }
    }

    fun updateAllAlbums() = intent {
        val albums = getAlbumList(context.contentResolver)
        reduce { GalleryListUiState.Granted(albumList = albums) }
    }

    fun updateUserSelectedAlbums() = intent {
        val albums = getAlbumList(context.contentResolver)
        reduce {
            when (state) {
                is GalleryListUiState.Partial -> (state as GalleryListUiState.Partial).copy(
                    albumList = albums
                )

                else -> GalleryListUiState.Partial(albumList = albums)
            }
        }
    }

    private fun getAlbumList(contentResolver: ContentResolver): List<Album> {
        val albumMap = mutableMapOf<String, Album>()
        val bucketCountMap = mutableMapOf<String, Int>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val bucketNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val albumId = cursor.getString(bucketIdColumn)
                val albumName = cursor.getString(bucketNameColumn) ?: "기타"
                val imageId = cursor.getLong(idColumn)
                val coverUri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    imageId.toString()
                )

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

    fun requestMediaPermissionModal() = intent {
        runOn<GalleryListUiState.Partial> {
            reduce {
                state.copy(showMediaPermissionModal = true)
            }
        }
    }

    fun dismissMediaPermissionModal() = intent {
        runOn<GalleryListUiState.Partial> {
            reduce {
                state.copy(showMediaPermissionModal = false)
            }
        }
    }

    fun requestMediaPermission() = intent {
        when (state) {
            is GalleryListUiState.Partial -> {
                reduce { (state as GalleryListUiState.Partial).copy(requestMediaPermission = true) }
            }

            is GalleryListUiState.Denied -> {
                reduce { (state as GalleryListUiState.Denied).copy(requestMediaPermission = true) }
            }

            else -> Unit
        }
    }

    fun resetMediaPermission() = intent {
        when (state) {
            is GalleryListUiState.Partial -> {
                reduce { (state as GalleryListUiState.Partial).copy(requestMediaPermission = false) }
            }

            is GalleryListUiState.Denied -> {
                reduce { (state as GalleryListUiState.Denied).copy(requestMediaPermission = false) }
            }

            else -> Unit
        }
    }

    fun requestMediaPermissionDialog() = intent {
        when (state) {
            is GalleryListUiState.Partial -> {
                reduce { (state as GalleryListUiState.Partial).copy(showMediaPermissionDialog = true) }
            }

            is GalleryListUiState.Denied -> {
                reduce { (state as GalleryListUiState.Denied).copy(showMediaPermissionDialog = true) }
            }

            else -> Unit
        }
    }

    fun dismissMediaPermissionDialog() = intent {
        when (state) {
            is GalleryListUiState.Partial -> {
                reduce { (state as GalleryListUiState.Partial).copy(showMediaPermissionDialog = false) }
            }

            is GalleryListUiState.Denied -> {
                reduce { (state as GalleryListUiState.Denied).copy(showMediaPermissionDialog = false) }
            }

            else -> Unit
        }
    }

    fun onPermissionSettingClick(packageName: String) = intent {
        when (state) {
            is GalleryListUiState.Partial,
            is GalleryListUiState.Denied -> {
                postSideEffect(GalleryListSideEffect.NavigateToSettings(packageName))
            }

            else -> Unit
        }
    }

    fun onClickAlbum(albumId: String, albumName: String) = intent {
        postSideEffect(GalleryListSideEffect.NavigateToAlbumGrid(albumId, albumName))
    }
}

sealed class GalleryListUiState {
    data object Loading : GalleryListUiState()

    @Immutable
    data class Granted(
        val albumList: List<Album> = emptyList()
    ) : GalleryListUiState()

    @Immutable
    data class Partial(
        val albumList: List<Album> = emptyList(),
        val requestMediaPermission: Boolean = false,
        val showMediaPermissionModal: Boolean = false,
        val showMediaPermissionDialog: Boolean = false
    ) : GalleryListUiState()

    data class Denied(
        val requestMediaPermission: Boolean = false,
        val showMediaPermissionDialog: Boolean = false
    ) : GalleryListUiState()
}

sealed interface GalleryListSideEffect {
    data class NavigateToSettings(val packageName: String) : GalleryListSideEffect
    data class NavigateToAlbumGrid(val albumId: String, val albumName: String) :
        GalleryListSideEffect
}

data class Album(
    val id: String,
    val name: String,
    val coverUri: Uri,
    val imageCount: Int
)