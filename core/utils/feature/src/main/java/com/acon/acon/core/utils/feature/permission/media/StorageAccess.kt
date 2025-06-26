package com.acon.acon.core.utils.feature.permission.media

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO

/**
 * On Android 14+ devices, users can grant full or partial access to their photo library for apps
 * requesting [READ_MEDIA_IMAGES] and/or [READ_MEDIA_VIDEO] permissions.
 * On older devices, the photo library access can either be full or denied
 */
enum class StorageAccess {
    GRANTED, Partial, Denied
}