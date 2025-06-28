package com.acon.acon.core.ui.permission.media

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.pm.PackageManager.PERMISSION_GRANTED
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
 *
 * @RequiresPermission(anyOf = [READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED, READ_EXTERNAL_STORAGE])
 * This app is not using Video Permission → (READ_MEDIA_VIDEO)
 *
 * @param onPermissionGranted → 미디어 접근권한이 Granted이면 실행되는 동작
 * @param onPermissionDenied  → 미디어 접근권한이 Denied이면  실행되는 동작
 * @param onPermissionPartial → 미디어 접근권한이 Partial이면 실행되는 동작 (제한적 접근 권한)
 * @param ignorePartialPermission → (제한적) 권한을 (항상 모두 허용)권한과 동일하게 처리할지 여부
 *                                      true: PARTIAL과 GRANTED를 동일하게 처리 (일반적인 경우)
 *                                      false: PARTIAL 별도 처리
 */
@Composable
fun CheckAndRequestMediaPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionPartial: () -> Unit = {},
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
                // 앱이 포그라운드로 돌아올 때마다 권한 상태를 최신으로 업데이트
                // 사용자가 설정 앱에서 권한을 변경했거나, 시스템 권한 다이얼로그에서 파일을 선택
                value = getStorageAccess(context)

                if ((value == StorageAccess.Partial) || (value == StorageAccess.Denied))
                    coroutineScope.launch {
                        // 제한적 접근 권한인 경우, 시스템은 유저가 직접 허용(선택)한 미디어 파일들만 쿼리하여 결과로 반환
                        // 이때, 유저가 이미지를 선택했다면, 앨범 UI를 갱신하는 콜백 호출
                        files = getVisualMedia(context.contentResolver, onPermissionPartial)
                    }
            }
        }
        lifecycleOwner.lifecycle.addObserver(eventObserver)
        awaitDispose { lifecycleOwner.lifecycle.removeObserver(eventObserver) }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
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
                        if (ignorePartialPermission) onPermissionGranted()
                    }

                    else -> {
                        onPermissionDenied()
                    }
                }
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                val granted = results[READ_MEDIA_IMAGES]
                    ?: (checkSelfPermission(context, READ_MEDIA_IMAGES) == PERMISSION_GRANTED)

                if (granted) onPermissionGranted()
                else onPermissionDenied()
            }

            else -> {
                val granted = results[READ_EXTERNAL_STORAGE]
                    ?: (checkSelfPermission(context, READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED)

                if (granted) onPermissionGranted()
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
                requestPermissionLauncher.launch(permission)
            }

            StorageAccess.Partial -> {
                if (ignorePartialPermission) {
                    onPermissionGranted()
                } else {
                    /* 권한이 제한된 액세스 허용인 경우, "더 많은 사진과 동영상에 액세스하도록 허용하시겠습니까?"
                       추가 권한 다이얼로그(시스템 UI) 를 한번 더 띄우는 코드 */
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        requestPermissionLauncher.launch(
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