package com.acon.android.feature.areaverification

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.android.core.designsystem.blur.LocalHazeState
import com.acon.android.core.designsystem.component.button.AconFilledLargeButton
import com.acon.android.core.designsystem.component.dialog.AconOneButtonDialog
import com.acon.android.core.designsystem.theme.AconTheme
import com.acon.android.core.utils.feature.action.BackOnPressed
import com.acon.android.core.utils.feature.permission.CheckAndRequestLocationPermission
import com.acon.android.feature.areaverification.component.AreaVerificationButton
import dev.chrisbanes.haze.hazeSource

@Composable
fun AreaVerificationScreenContainer(
    onNewAreaClick: (Double, Double) -> Unit,
    onNextScreen: (Double, Double) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AreaVerificationViewModel = hiltViewModel(),
) {
    val state by viewModel.container.stateFlow.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { effect ->
            when (effect) {
                is AreaVerificationSideEffect.NavigateToSettings -> {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", effect.packageName, null)
                    }
                    context.startActivity(intent)
                }

                is AreaVerificationSideEffect.NavigateToNextScreen -> {
                    onNextScreen(effect.latitude, effect.longitude)
                }

                is AreaVerificationSideEffect.NavigateToNewArea -> {
                    onNewAreaClick(effect.latitude, effect.longitude)
                }
            }
        }
    }

    CheckAndRequestLocationPermission {
        viewModel.checkLocationAndNavigate()
    }

    AreaVerificationScreen(
        state = state,
        onNewLocationSelected = { viewModel.onNewLocationSelected() },
        onDismissPermissionDialog = { viewModel.updateShowPermissionDialog(false) },
        onPermissionSettingClick = { viewModel.onPermissionSettingClick(context.packageName) },
        onNextButtonClick = { viewModel.onNextButtonClick() },
        modifier = modifier
    )
}

@Composable
fun AreaVerificationScreen(
    state: AreaVerificationState,
    onNewLocationSelected: () -> Unit,
    onDismissPermissionDialog: () -> Unit,
    onPermissionSettingClick: () -> Unit,
    onNextButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    BackOnPressed(context)

    if (state.showPermissionDialog) {
        AconOneButtonDialog(
            title = stringResource(R.string.permission_dialog_title),
            content = stringResource(R.string.permission_dialog_content),
            buttonContent = stringResource(R.string.go_to_settings),
            onDismissRequest = onDismissPermissionDialog,
            onClickConfirm = onPermissionSettingClick,
            isImageEnabled = false
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AconTheme.color.Gray9)
            .hazeSource(LocalHazeState.current)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(132.dp))

            Text(
                text = stringResource(R.string.local_restaurant_certification_title),
                style = AconTheme.typography.head5_22_sb,
                color = AconTheme.color.Gray1
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.local_restaurant_certification_description),
                style = AconTheme.typography.subtitle2_14_med,
                color = AconTheme.color.Gray3
            )

            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AreaVerificationButton(
                    selected = state.isNewLocationSelected,
                    onClick = onNewLocationSelected,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        AconFilledLargeButton(
            text = stringResource(R.string.next),
            textStyle = AconTheme.typography.head8_16_sb,
            enabledBackgroundColor = AconTheme.color.Gray5,
            disabledBackgroundColor = AconTheme.color.Gray8,
            enabledTextColor = AconTheme.color.White,
            disabledTextColor = AconTheme.color.Gray6,
            onClick = onNextButtonClick,
            isEnabled = state.isNewLocationSelected,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
    }
}
