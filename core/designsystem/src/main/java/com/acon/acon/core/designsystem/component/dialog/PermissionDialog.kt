package com.acon.acon.core.designsystem.component.dialog

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.dialog.v2.AconDefaultDialog
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconPermissionDialog(
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

    AconDefaultDialog (
        title = stringResource(R.string.no_permission_title),
        action = stringResource(R.string.go_to_setting),
        onDismissRequest = {},
        onAction = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            settingsLauncher.launch(intent)
        }
    ) {
        Text(
            text = stringResource(R.string.no_permission_content),
            style = AconTheme.typography.Body1,
            fontWeight = FontWeight.W400,
            color = AconTheme.color.Gray200,
        )
        Spacer(modifier = Modifier.height(22.dp))
    }
}

@Preview
@Composable
private fun AconPermissionDialogPreview() {
    AconPermissionDialog()
}