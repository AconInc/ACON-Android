package com.acon.acon.core.utils.feature.permission

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 위치 권한을 확인하고 요청하는 컴포저블
 * @param onPermissionGranted 권한이 허용되었을 때 실행할 동작
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckAndRequestLocationPermission(
    onPermissionGranted: () -> Unit
) {
    var trigger by rememberSaveable { mutableIntStateOf(0) }
    var showPermissionDialog by rememberSaveable { mutableStateOf(false) }

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
        onPermissionsResult = { permMap ->
            if (permMap.isNotEmpty())
                trigger = (trigger + 1).coerceAtMost(2)
        }
    )
    if (showPermissionDialog)
        AconPermissionDialog(
            onPermissionGranted = {
                showPermissionDialog = false
                onPermissionGranted()
            }
        )

    LaunchedEffect(trigger) {
        withContext(Dispatchers.Main.immediate) {
            if (locationPermissionState.allPermissionsGranted) {
                onPermissionGranted()
            } else {
                if (locationPermissionState.shouldShowRationale) {
                    showPermissionDialog = true
                } else {
                    if (trigger == 2) {
                        showPermissionDialog = true
                    } else locationPermissionState.launchMultiplePermissionRequest()
                }
            }
        }
    }
}