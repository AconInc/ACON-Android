package com.acon.core.utils.feature.permission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.acon.core.designsystem.component.dialog.AconTwoButtonDialog
import com.acon.core.utils.feature.R
import android.provider.Settings
import android.net.Uri

@Composable
internal fun AconPhotoPermissionDialog(
    onPermissionGranted: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onPermissionGranted()
        }
    }

    AconTwoButtonDialog(
        title = stringResource(R.string.photo_permission_alert_title),
        content = stringResource(R.string.photo_permission_alert_subtitle),
        leftButtonContent = stringResource(R.string.photo_permission_alert_left_btn),
        rightButtonContent = stringResource(R.string.photo_permission_alert_right_btn),
        contentImage = 0,
        onDismissRequest = { //다이얼로그 닫기
            onDismiss()
        },
        onClickLeft = { // 다이얼로그 닫기
            onDismiss()
        },
        onClickRight = { // 사진 권한 설정으로 이동하기
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            settingsLauncher.launch(intent)
        },
        isImageEnabled = false
    )

}
