package com.acon.acon.core.utils.feature.permission.media

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
 */
suspend fun getVisualMedia(contentResolver: ContentResolver): List<MediaEntry> {
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

            while (cursor.moveToNext()) {
                val uri = ContentUris.withAppendedId(collectionUri, cursor.getLong(idColumn))
                val name = cursor.getString(displayNameColumn)
                val size = cursor.getLong(sizeColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val dateAdded = cursor.getLong(dateAddedColumn)

                visualMedia.add(MediaEntry(uri, name, size, mimeType, dateAdded))
            }
        }
        return@withContext visualMedia
    }
}