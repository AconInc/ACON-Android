package com.acon.acon.core.utils.feature.permission.media

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.launch

/**
 * name = "CheckAndRequestMediaPermission",
 * description = "Check and request storage(media) permissions",
 * documentation = "https://developer.android.com/about/versions/14/changes/partial-photo-video-access",
 * reference = https://github.com/android/platform-samples/tree/main/samples/storage/src/main/java/com/example/platform/storage
 *
 * @RequiresPermission(anyOf = [READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED, READ_EXTERNAL_STORAGE])
 * This app is not using Video Permission → (READ_MEDIA_VIDEO)
 */
@Composable
fun CheckAndRequestMediaPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    var files by remember { mutableStateOf(emptyList<MediaEntry>()) }

    val storageAccess by produceState(
        initialValue = StorageAccess.Denied,
        context,
        lifecycleOwner
    ) {
        val eventObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                value = getStorageAccess(context)

                if (value == StorageAccess.Partial) {
                    coroutineScope.launch {
                        files = getVisualMedia(context.contentResolver)
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(eventObserver)
        awaitDispose { lifecycleOwner.lifecycle.removeObserver(eventObserver) }
    }

    val requestPermissions = rememberLauncherForActivityResult(
        contract = RequestMultiplePermissions()
    ) { results: Map<String, Boolean> ->
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                val granted =
                    results[READ_MEDIA_IMAGES] == true && results[READ_MEDIA_VISUAL_USER_SELECTED] == true

                when {
                    granted -> onPermissionGranted()
                    else -> onPermissionDenied()
                }
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                if (results[READ_MEDIA_IMAGES] == true) onPermissionGranted()
                else onPermissionDenied()
            }

            else -> {
                if (results[READ_EXTERNAL_STORAGE] == true) onPermissionGranted()
                else onPermissionDenied()
            }
        }
    }

    LaunchedEffect(storageAccess) {
        when (storageAccess) {
            StorageAccess.GRANTED, StorageAccess.Partial -> {
                onPermissionGranted()
            }

            StorageAccess.Denied -> {
                val permission = when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE ->
                        arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED)

                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                        arrayOf(READ_MEDIA_IMAGES)

                    else ->
                        arrayOf(READ_EXTERNAL_STORAGE)
                }
                requestPermissions.launch(permission)
            }
        }
    }
}