package com.acon.acon.core.utils.feature.update

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Application
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.dialog.v2.AconOneActionDialog
import com.acon.acon.core.designsystem.component.dialog.v2.AconTwoActionDialog
import com.acon.acon.core.utils.feature.intent.launchPlayStore
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun AppUpdateChecker(
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<AppUpdateViewModel, AppUpdateViewModel.AppUpdateViewModelFactory>(
        creationCallback = { factory ->
            factory.create(application = context.applicationContext as Application)
        })

    val updateState by viewModel.updateState.collectAsStateWithLifecycle()
    val appUpdateManager = viewModel.appUpdateManager
    val appUpdateInfo by viewModel.appUpdateInfo.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val listener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = context.getString(R.string.update_complete),
                    actionLabel = context.getString(R.string.restart)
                )
                when(result) {
                    SnackbarResult.ActionPerformed -> {
                        appUpdateManager.completeUpdate()
                    }
                    SnackbarResult.Dismissed -> Unit
                }
            }
        }
    }

    val appUpdateActivityResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {   // Immediate에서는 받을 일 없음
                Timber.d("유저 업데이트 수락")
            } else if (result.resultCode == RESULT_CANCELED) {
                Timber.d("유저 업데이트 거부")
            } else if (result.resultCode == ActivityResult.RESULT_IN_APP_UPDATE_FAILED) {
                Timber.d("업데이트 실패")
            }
        }

    appUpdateInfo?.let { updateInfo ->
        when (updateState) {
            UpdateState.FORCE -> {
                AconOneActionDialog(
                    title = stringResource(R.string.update_required_title),
                    action = stringResource(R.string.update),
                    onAction = { context.launchPlayStore() },
                    onDismissRequest = {}
                )
            }

            UpdateState.OPTIONAL -> {
                AconTwoActionDialog(
                    title = stringResource(R.string.update_available_title),
                    action1 = stringResource(R.string.cancel),
                    action2 = stringResource(R.string.update),
                    onAction1 = {
                        viewModel.setUpdateState(UpdateState.NONE)
                    }, onAction2 = {
                        appUpdateManager.startUpdateFlowForResult(
                            updateInfo,
                            appUpdateActivityResultLauncher,
                            AppUpdateOptions.defaultOptions(AppUpdateType.FLEXIBLE)
                        )
                    }, onDismissRequest = {
                        viewModel.setUpdateState(UpdateState.NONE)
                    }
                )
            }

            UpdateState.NONE -> Unit
        }
    }

    DisposableEffect(appUpdateManager) {
        appUpdateManager.registerListener(listener)
        onDispose {
            appUpdateManager.unregisterListener(listener)
        }
    }
}