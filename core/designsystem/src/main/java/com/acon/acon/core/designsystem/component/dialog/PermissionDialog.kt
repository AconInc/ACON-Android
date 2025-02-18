package com.acon.acon.core.designsystem.component.dialog

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.acon.acon.core.designsystem.R

@Composable
fun AconPermissionDialog(
    modifier: Modifier = Modifier,
    onPermissionGranted: () -> Unit = {}
) {

    val context = LocalContext.current
    val settingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) { onPermissionGranted() }
    }

    AconOneButtonDialog(
        title = stringResource(R.string.no_permission_title),
        content = stringResource(R.string.no_permission_content),
        buttonContent = stringResource(R.string.go_to_setting),
        onDismissRequest = {},
        onClickConfirm = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            settingsLauncher.launch(intent)
        }
    )
}

@Preview
@Composable
private fun AconPermissionDialogPreview() {
    AconPermissionDialog()
}