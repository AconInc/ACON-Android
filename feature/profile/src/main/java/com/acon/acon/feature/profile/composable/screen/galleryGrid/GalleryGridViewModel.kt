package com.acon.acon.feature.profile.composable.screen.galleryGrid

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.acon.acon.core.ui.permission.media.StorageAccess
import com.acon.acon.core.ui.permission.media.getStorageAccess
import com.acon.acon.core.navigation.route.ProfileRoute
import com.acon.acon.core.ui.base.BaseContainerHost
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    private val imageLoadingDispatcher = Dispatchers.IO.limitedParallelism(4)

    override val container =
        container<GalleryGridUiState, GalleryGridSideEffect>(GalleryGridUiState.Loading) {
            updateStorageAccess()
        }

    fun updateStorageAccess() = intent {
        when (getStorageAccess(context)) {
            StorageAccess.GRANTED -> {
                try {
                    // 백그라운드에서 이미지 로드
                    allPhotos = withContext(imageLoadingDispatcher) {
                        getAllPhotos(albumId)
                    }
                    page = 0

                    val initialPhotos = allPhotos.take(pageSize)
                    loadedPhotos = persistentListOf<Uri>().addAll(initialPhotos)

                    // UI 업데이트는 메인 스레드에서 진행
                    reduce {
                        GalleryGridUiState.Granted(photoList = initialPhotos)
                    }
                    page = 1
                } catch (e: Exception) {
                    reduce { GalleryGridUiState.Denied() }
                }
            }
            StorageAccess.Partial -> {
                try {
                    val photos = withContext(imageLoadingDispatcher) {
                        getAllPhotos(albumId)
                    }
                    reduce { GalleryGridUiState.Partial(photoList = photos) }
                } catch (e: Exception) {
                    //reduce { GalleryGridUiState.Denied() }
                }
            }
            StorageAccess.Denied -> reduce { GalleryGridUiState.Denied() }
        }
    }

    fun loadNextPage() = intent {
        if (allPhotos.isEmpty()) return@intent

        val startIndex = page * pageSize
        val endIndex = (page + 1) * pageSize
        if (startIndex >= allPhotos.size) return@intent

        // 백그라운드에서 다음 페이지 로드
        val newPhotos = withContext(imageLoadingDispatcher) {
            allPhotos.subList(startIndex, endIndex.coerceAtMost(allPhotos.size))
        }
        val updated = loadedPhotos.addAll(newPhotos)

        reduce {
            when (val current = state) {
                is GalleryGridUiState.Granted -> {
                    loadedPhotos = updated
                    current.copy(photoList = loadedPhotos)
                }
                else -> current
            }
        }
        page += 1
    }

    fun updateAllPhotos() = intent {
        val photoList = withContext(imageLoadingDispatcher) {
            getAllPhotos(albumId)
        }
        reduce { GalleryGridUiState.Granted(photoList = photoList) }
    }

    fun updateUserSelectedPhotos() = intent {
        val photoList = withContext(imageLoadingDispatcher) {
            getAllPhotos(albumId)
        }
        reduce {
            when (state) {
                is GalleryGridUiState.Partial -> (state as GalleryGridUiState.Partial).copy(
                    photoList = photoList
                )
                else -> GalleryGridUiState.Partial(photoList = photoList)
            }
        }
    }

    private suspend fun getAllPhotos(albumId: String): List<Uri> = withContext(imageLoadingDispatcher) {
        val photoList = mutableListOf<Uri>()

        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(albumId)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        try {
            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val uriList = mutableListOf<Long>()

                while (cursor.moveToNext()) {
                    val imageId = cursor.getLong(idColumn)
                    val uri = Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        imageId.toString()
                    )
                    photoList.add(uri)
                }
            }
        } catch (e: Exception) { }

        photoList
    }

    // 기존의 동기 메서드 (호환성을 위해 유지)
//    private fun getAllPhotos(albumId: String): List<Uri> {
//        val photoList = mutableListOf<Uri>()
//
//        val projection = arrayOf(MediaStore.Images.Media._ID)
//        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
//        val selectionArgs = arrayOf(albumId)
//        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
//
//        context.contentResolver.query(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            projection,
//            selection,
//            selectionArgs,
//            sortOrder
//        )?.use { cursor ->
//            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
//
//            while (cursor.moveToNext()) {
//                val imageId = cursor.getLong(idColumn)
//                val photoUri = Uri.withAppendedPath(
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    imageId.toString()
//                )
//                photoList.add(photoUri)
//            }
//        }
//
//        return photoList
//    }

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