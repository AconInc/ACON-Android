package com.acon.acon.core.utils.feature.permission

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

/**
 * 사진 접근 권한을 확인하고 요청하는 컴포저블
 * @param onPermissionGranted 권한이 허용되었을 때 실행할 동작
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckAndRequestPhotoPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
) {

    val permission = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> android.Manifest.permission.READ_MEDIA_IMAGES
        else -> android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val photoPermissionState = rememberPermissionState(permission)

    when {
        photoPermissionState.status.isGranted -> {
            onPermissionGranted()
        }
        photoPermissionState.status.shouldShowRationale -> {
            onPermissionDenied()
        }
        else -> { //여기로 들어오는데 왜 안뜨나..
            LaunchedEffect(Unit){
                photoPermissionState.launchPermissionRequest()
            }
        }
    }
}
