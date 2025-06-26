package com.acon.core.ui.permission.media

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Files
import android.provider.MediaStore.Files.FileColumns
import androidx.core.content.ContextCompat.checkSelfPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Depending on the version of Android the device is running, the app should request the right
 * storage permissions:
 * Up to Android 12L    -> [READ_EXTERNAL_STORAGE]
 * Android 13           -> [READ_MEDIA_IMAGES] and/or [READ_MEDIA_VIDEO]
 * Android 14+          -> Partial access sets only [READ_MEDIA_VISUAL_USER_SELECTED] to granted
 *                      -> Full access sets [READ_MEDIA_IMAGES] and/or [READ_MEDIA_VIDEO] to granted
 */
fun getStorageAccess(context: Context): StorageAccess {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
            val readImage = checkSelfPermission(context, READ_MEDIA_IMAGES) == PERMISSION_GRANTED
            val readImageUserSelect =
                checkSelfPermission(context, READ_MEDIA_VISUAL_USER_SELECTED) == PERMISSION_GRANTED

            when {
                readImage -> StorageAccess.GRANTED
                readImageUserSelect -> StorageAccess.Partial
                else -> StorageAccess.Denied
            }
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            if (checkSelfPermission(
                    context,
                    READ_MEDIA_IMAGES
                ) == PERMISSION_GRANTED
            ) StorageAccess.GRANTED
            else StorageAccess.Denied
        }

        else -> {
            if (checkSelfPermission(
                    context,
                    READ_EXTERNAL_STORAGE
                ) == PERMISSION_GRANTED
            ) StorageAccess.GRANTED
            else StorageAccess.Denied
        }
    }
}

/**
 * Query [MediaStore] through [ContentResolver] to get all images & videos sorted by most added date
 * by targeting the [Files] collection
 * 기기의 외부 저장소에서 이미지 파일들을 쿼리해서 MediaEntry 리스트로 반환하는 함수
 * - 전체 권한: 기기의 모든 이미지가 반환
 * - 제한적 권한: 사용자가 선택한 이미지들만 반환
 * - 권한 없음: 빈 리스트 반환
 * @param contentResolver MediaStore 접근을 위한 ContentResolver
 * @param onPermissionPartial 제한적 권한 상태에서 이미지가 발견되었을 때 호출될 콜백
 */
suspend fun getVisualMedia(
    contentResolver: ContentResolver,
    onPermissionPartial: (() -> Unit)? = null
): List<MediaEntry> {
    return withContext(Dispatchers.IO) {
        val projection = arrayOf(
            FileColumns._ID,
            FileColumns.DISPLAY_NAME,
            FileColumns.SIZE,
            FileColumns.MIME_TYPE,
            FileColumns.DATE_ADDED
        )

        val collectionUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            Files.getContentUri("external")
        }

        val visualMedia = mutableListOf<MediaEntry>()

        // MediaStore를 통해 모든 이미지 파일을 쿼리 (제한적 권한일 경우 시스템이 허용된 파일만 반환)
        contentResolver.query(
            collectionUri,
            projection,
            "${FileColumns.MEDIA_TYPE} = ? OR ${FileColumns.MEDIA_TYPE} = ?",
            arrayOf(
                FileColumns.MEDIA_TYPE_IMAGE.toString(),
            ),
            "${FileColumns.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(FileColumns._ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(FileColumns.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(FileColumns.SIZE)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(FileColumns.MIME_TYPE)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(FileColumns.DATE_ADDED)

            // 쿼리 결과를 순회하면서 MediaEntry 객체로 변환
            while (cursor.moveToNext()) {
                val uri = ContentUris.withAppendedId(collectionUri, cursor.getLong(idColumn))
                val name = cursor.getString(displayNameColumn)
                val size = cursor.getLong(sizeColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val dateAdded = cursor.getLong(dateAddedColumn)

                visualMedia.add(MediaEntry(uri, name, size, mimeType, dateAdded))
            }
        }

        // 제한적 권한 상태에서 사용자가 선택한 이미지가 있다면 UI 갱신을 위한 콜백 호출
        if (visualMedia.isNotEmpty()) {
            onPermissionPartial?.invoke()
        }

        return@withContext visualMedia
    }
}