package com.acon.acon.feature.areaverification.composable

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.dialog.v2.AconDefaultDialog
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun PreferenceMapScreen(
    latitude: Double,
    longitude: Double,
    isEdit: Boolean,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNavigateToNext: () -> Unit = {},
    viewModel: AreaVerificationViewModel = hiltViewModel()
) {
    var currentLatitude by remember { mutableDoubleStateOf(latitude) }
    var currentLongitude by remember { mutableDoubleStateOf(longitude) }

    val context = LocalContext.current
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        //viewModel.fetchVerifiedArea()
        viewModel.checkDeviceGPSStatus()
        viewModel.checkSupportLocation(context)

        viewModel.container.sideEffectFlow.collect { effect ->
            when (effect) {
                is AreaVerificationHomeSideEffect.NavigateToSystemLocationSettings -> {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(intent)
                }

                else -> {}
            }
        }
    }

    LaunchedEffect(state.isGPSEnabled) {
        viewModel.checkDeviceGPSStatus()
        viewModel.checkSupportLocation(context)
    }

    LaunchedEffect(state.isVerifySuccess) {
        if (state.isVerifySuccess) {
            onNavigateToNext()
        }
    }

    if (state.showDeviceGPSDialog) {
        AconDefaultDialog(
            title = stringResource(R.string.location_permission_denied_title),
            action = stringResource(R.string.go_to_setting),
            onAction = {
                viewModel.onDeviceGPSSettingClick(context.packageName)
            },
            onDismissRequest = {},
            content = {
                Text(
                    text = stringResource(R.string.location_permission_denied_content),
                    style = AconTheme.typography.Title4,
                    color = AconTheme.color.White,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 20.dp)
                )
            }
        )
    }

    if (state.showLocationDialog) {
        AconDefaultDialog(
            title = stringResource(R.string.location_unsupported_area_title),
            action = stringResource(R.string.ok),
            onDismissRequest = {},
            onAction = { (context as? Activity)?.finishAffinity() },
            content = {
                Text(
                    text = stringResource(R.string.location_unsupported_area_content),
                    style = AconTheme.typography.Title4,
                    color = AconTheme.color.White,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 20.dp)
                )
            }
        )
    }

    Column(
        modifier = modifier
            .background(
                color = AconTheme.color.DimDefault.copy(alpha = 0.8f)
            )
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        AconTopBar(
            leadingIcon = {
                IconButton(onClick = onBackClick) {
                    Image(
                        imageVector = ImageVector.vectorResource(
                            id = R.drawable.ic_left
                        ),
                        contentDescription = stringResource(R.string.back)
                    )
                }
            },
            content = {
                Text(
                    text = stringResource(R.string.area_verification_topbar),
                    style = AconTheme.typography.Title4,
                    color = AconTheme.color.White
                )
            },
            modifier = Modifier
                .background(
                    color = AconTheme.color.DimDefault.copy(alpha = 0.8f)
                )
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            LocationMapScreen(
                onLocationObtained = { lat, lng ->
                    currentLatitude = lat
                    currentLongitude = lng
                },
                initialLatitude = latitude,
                initialLongitude = longitude,
                modifier = Modifier.fillMaxSize(),
                onClickConfirm = {
                    if (isEdit) {
                        viewModel.editVerifiedArea(
                            latitude = currentLatitude,
                            longitude = currentLongitude
                        )
                    } else {
                        viewModel.verifyArea(currentLatitude, currentLongitude)
                    }
                }
            )
        }
    }
}
