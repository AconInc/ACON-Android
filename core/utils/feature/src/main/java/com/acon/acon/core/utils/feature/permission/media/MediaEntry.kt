package com.acon.acon.core.utils.feature.permission.media

import android.net.Uri

data class MediaEntry(
    val uri: Uri,
    val name: String,
    val size: Long,
    val mimeType: String,
    val dateAdded: Long
)