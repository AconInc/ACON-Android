package com.acon.acon.core.utils.feature.permission.media

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.util.Log
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
import androidx.core.content.ContextCompat.checkSelfPermission
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
 *
 * onPermissionGranted → 미디어 접근권한이 Granted이면 실행되는 동작
 * onPermissionDenied →  미디어 접근권한이 Denied이면 실행되는 동작
 * ignorePartialPermission → 미디어 접근 권한이 Granted, Partial 상태에서의 동작이 서로 다르게 동작해야 할 때 사용되는 플래그 (갤러리 내부에서 사용)
 */
@Composable
fun CheckAndRequestMediaPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    ignorePartialPermission: Boolean = true
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
                val readMediaImage = results[READ_MEDIA_IMAGES]
                val readVisualUserSelected = results[READ_MEDIA_VISUAL_USER_SELECTED]

                val granted = if (readMediaImage != null) {
                    results[READ_MEDIA_IMAGES] == true
                } else {
                    checkSelfPermission(context, READ_MEDIA_IMAGES) == PERMISSION_GRANTED
                }

                val partial = if (readVisualUserSelected != null) {
                    results[READ_MEDIA_VISUAL_USER_SELECTED] == true
                } else {
                    checkSelfPermission(
                        context,
                        READ_MEDIA_VISUAL_USER_SELECTED
                    ) == PERMISSION_GRANTED
                }

                when {
                    granted -> {
                        onPermissionGranted()
                    }

                    partial -> {
                        Log.d("로그", "requestPermissions, partial")
                        if (ignorePartialPermission) onPermissionGranted()
                    }

                    else -> {
                        Log.d("로그", "requestPermissions, denied")
                        onPermissionDenied()
                    }
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
            StorageAccess.GRANTED -> {
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

            StorageAccess.Partial -> {
                if (ignorePartialPermission) {
                    onPermissionGranted()
                } else {
                    /* 제한적 접근 권한 상태에서 "더 많은 사진과 동영상에 액세스하도록 허용하시겠습니까?"
                       시스템 권한 다이얼로그를 한번 더 띄우는 코드 */
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        Log.d("로그", "StorageAccess.Partial → 시스템 권한 다이얼로그를 한번 더 요청")
                        requestPermissions.launch(
                            arrayOf(
                                READ_MEDIA_IMAGES,
                                READ_MEDIA_VISUAL_USER_SELECTED
                            )
                        )
                    }
                }
            }
        }
    }
}