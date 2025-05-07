package com.acon.feature.common.permission

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.acon.acon.core.designsystem.component.dialog.AconPermissionDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionRequester(
    onPermissionGranted: () -> Unit,
) {
    var requestCount by rememberSaveable { mutableIntStateOf(0) }
    var showPermissionDialog by rememberSaveable { mutableStateOf(false) }

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
        onPermissionsResult = { _ ->
            requestCount = maxOf(requestCount + 1, 2)
        }
    )
    if (showPermissionDialog)
        AconPermissionDialog(
            onPermissionGranted = {
                showPermissionDialog = false
                onPermissionGranted()
            }
        )

    LaunchedEffect(requestCount) {
        if (locationPermissionState.allPermissionsGranted) {
            onPermissionGranted()
        } else {
            if (locationPermissionState.shouldShowRationale) {
                locationPermissionState.launchMultiplePermissionRequest()
            } else {
                if (requestCount == 2) {
                    showPermissionDialog = true
                } else locationPermissionState.launchMultiplePermissionRequest()
            }
        }
    }
}