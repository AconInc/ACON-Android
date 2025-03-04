package com.acon.acon.core.utils.feature.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.acon.acon.core.designsystem.component.dialog.AconPermissionDialog
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
    enableDialog: Boolean = true,
    onPermissionGranted: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var showPermissionDialog by rememberSaveable { mutableStateOf(false) }
    var isPermissionGranted by remember { mutableStateOf(context.checkLocationPermission()) }

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
        onPermissionsResult = { permMap ->
            isPermissionGranted = permMap.values.all { it }
        }
    )
    if (showPermissionDialog && enableDialog)
        AconPermissionDialog(
            onPermissionGranted = {
                showPermissionDialog = false
                onPermissionGranted()
            }
        )

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isPermissionGranted = context.checkLocationPermission()
                if (!isPermissionGranted) {
                    showPermissionDialog = true
                } else {
                    showPermissionDialog = false
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(isPermissionGranted) {
        if (isPermissionGranted) {
            onPermissionGranted()
        } else {
            if (locationPermissionState.shouldShowRationale || !locationPermissionState.allPermissionsGranted) {
                showPermissionDialog = true
            }
        }
    }
}

fun Context.checkLocationPermission(): Boolean {
    val fineLocationPermission = ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val coarseLocationPermission = ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    return fineLocationPermission && coarseLocationPermission
}
