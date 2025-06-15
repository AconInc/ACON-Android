package com.acon.acon.feature.profile.composable.screen.galleryGrid

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.acon.acon.core.utils.feature.permission.media.StorageAccess
import com.acon.acon.core.utils.feature.permission.media.getStorageAccess
import com.acon.acon.feature.profile.composable.ProfileRoute
import com.acon.feature.common.base.BaseContainerHost
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class GalleryGridViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : BaseContainerHost<GalleryGridUiState, GalleryGridSideEffect>() {

    private val galleryGridRoute = savedStateHandle.toRoute<ProfileRoute.GalleryGrid>()
    private val albumId = galleryGridRoute.albumId
    internal val albumName = galleryGridRoute.albumName

    private var page = 0
    private val pageSize = 20
    private var allPhotos: List<Uri> = emptyList()
    private var loadedPhotos: PersistentList<Uri> = persistentListOf()

    override val container =
        container<GalleryGridUiState, GalleryGridSideEffect>(GalleryGridUiState.Loading) {
            updateStorageAccess()
        }

    fun updateStorageAccess() = intent {
        when (getStorageAccess(context)) {
            StorageAccess.GRANTED -> {
                allPhotos = getAllPhotos(albumId)
                page = 0

                val initialPhotos = allPhotos.take(pageSize)
                loadedPhotos = persistentListOf<Uri>().addAll(initialPhotos)

                reduce {
                    GalleryGridUiState.Granted(photoList = initialPhotos)
                }

                page = 1
            }

            StorageAccess.Partial -> {
                reduce { GalleryGridUiState.Partial(photoList = getAllPhotos(albumId)) }
            }

            StorageAccess.Denied -> reduce { GalleryGridUiState.Denied() }
        }
    }

    fun loadNextPage() = intent {
        if (allPhotos.isEmpty()) return@intent

        val startIndex = page * pageSize
        val endIndex = (page + 1) * pageSize
        if (startIndex >= allPhotos.size) return@intent

        val newPhotos = allPhotos.subList(startIndex, endIndex.coerceAtMost(allPhotos.size))
        val updated = loadedPhotos.addAll(newPhotos)

        reduce {
            when (val current = state) {
                is GalleryGridUiState.Granted -> {
                    loadedPhotos = updated // 꼭 갱신!
                    current.copy(photoList = loadedPhotos)
                }
                else -> current
            }
        }

        page += 1
    }

    fun updateAllImages() = intent {
        val photoList = getAllPhotos(albumId)
        reduce { GalleryGridUiState.Granted(photoList = photoList) }
    }

    fun updateUserSelectedImages() = intent {
        val photoList = getAllPhotos(albumId)
        reduce {
            when (state) {
                is GalleryGridUiState.Partial -> (state as GalleryGridUiState.Partial).copy(
                    photoList = photoList
                )

                else -> GalleryGridUiState.Partial(photoList = photoList)
            }
        }
    }

    private fun getAllPhotos(albumId: String): List<Uri> {
        val photoList = mutableListOf<Uri>()

        val projection = arrayOf(MediaStore.Images.Media._ID)
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
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val imageId = cursor.getLong(idColumn)
                val photoUri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    imageId.toString()
                )
                photoList.add(photoUri)
            }
        }

        return photoList
    }

    fun requestMediaPermissionModal() = intent {
        runOn<GalleryGridUiState.Partial> {
            reduce {
                state.copy(showMediaPermissionModal = true)
            }
        }
    }

    fun dismissMediaPermissionModal() = intent {
        runOn<GalleryGridUiState.Partial> {
            reduce {
                state.copy(showMediaPermissionModal = false)
            }
        }
    }

    fun requestMediaPermission() = intent {
        when (state) {
            is GalleryGridUiState.Partial -> {
                reduce { (state as GalleryGridUiState.Partial).copy(requestMediaPermission = true) }
            }

            is GalleryGridUiState.Denied -> {
                reduce { (state as GalleryGridUiState.Denied).copy(requestMediaPermission = true) }
            }

            else -> Unit
        }
    }

    fun resetMediaPermission() = intent {
        when (state) {
            is GalleryGridUiState.Partial -> {
                reduce { (state as GalleryGridUiState.Partial).copy(requestMediaPermission = false) }
            }

            is GalleryGridUiState.Denied -> {
                reduce { (state as GalleryGridUiState.Denied).copy(requestMediaPermission = false) }
            }

            else -> Unit
        }
    }

    fun requestMediaPermissionDialog() = intent {
        when (state) {
            is GalleryGridUiState.Partial -> {
                reduce { (state as GalleryGridUiState.Partial).copy(showMediaPermissionDialog = true) }
            }

            is GalleryGridUiState.Denied -> {
                reduce { (state as GalleryGridUiState.Denied).copy(showMediaPermissionDialog = true) }
            }

            else -> Unit
        }
    }

    fun dismissMediaPermissionDialog() = intent {
        when (state) {
            is GalleryGridUiState.Partial -> {
                reduce { (state as GalleryGridUiState.Partial).copy(showMediaPermissionDialog = false) }
            }

            is GalleryGridUiState.Denied -> {
                reduce { (state as GalleryGridUiState.Denied).copy(showMediaPermissionDialog = false) }
            }

            else -> Unit
        }
    }

    fun onPhotoSelected(photoUri: Uri) = intent {
        reduce {
            when (state) {
                is GalleryGridUiState.Granted -> {
                    (state as GalleryGridUiState.Granted).copy(selectedPhotoUri = photoUri)
                }

                is GalleryGridUiState.Partial -> {
                    (state as GalleryGridUiState.Partial).copy(selectedPhotoUri = photoUri)
                }

                else -> state
            }
        }
    }

    fun onPermissionSettingClick(packageName: String) = intent {
        when (state) {
            is GalleryGridUiState.Partial,
            is GalleryGridUiState.Denied -> {
                postSideEffect(GalleryGridSideEffect.NavigateToSettings(packageName))
            }

            else -> Unit
        }
    }

    fun onConfirmSelected(photoUri: String) = intent {
        postSideEffect(GalleryGridSideEffect.NavigateToPhotoCropScreen(photoUri))
    }
}

sealed class GalleryGridUiState {
    data object Loading : GalleryGridUiState()

    @Immutable
    data class Granted(
        val photoList: List<Uri> = emptyList(),
        val selectedPhotoUri: Uri? = null
    ) : GalleryGridUiState()

    @Immutable
    data class Partial(
        val photoList: List<Uri> = emptyList(),
        val selectedPhotoUri: Uri? = null,
        val requestMediaPermission: Boolean = false,
        val showMediaPermissionModal: Boolean = false,
        val showMediaPermissionDialog: Boolean = false
    ) : GalleryGridUiState()

    data class Denied(
        val requestMediaPermission: Boolean = false,
        val showMediaPermissionDialog: Boolean = false
    ) : GalleryGridUiState()
}

sealed interface GalleryGridSideEffect {
    data class NavigateToSettings(val packageName: String) : GalleryGridSideEffect
    data class NavigateToPhotoCropScreen(val photoUri: String) : GalleryGridSideEffect
}