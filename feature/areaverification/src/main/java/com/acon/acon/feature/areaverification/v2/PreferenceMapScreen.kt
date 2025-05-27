package com.acon.acon.feature.areaverification.v2

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
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
import com.acon.acon.core.designsystem.component.dialog.v2.AconDefaultDialog
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.areaverification.R

@Composable
internal fun PreferenceMapScreen(
    latitude: Double,
    longitude: Double,
    isEdit: Boolean,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNavigateToNext: () -> Unit = {},
    viewModel: AreaVerificationHomeViewModel = hiltViewModel()
) {
    var currentLatitude by remember { mutableDoubleStateOf(latitude) }
    var currentLongitude by remember { mutableDoubleStateOf(longitude) }

    val context = LocalContext.current
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchVerifiedArea()
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

    if (state.showDeviceGPSDialog) {
        AconDefaultDialog(
            title = stringResource(R.string.location_check_fail_dialog_title),
            action = "확인",
            onAction = {
                viewModel.onDeviceGPSSettingClick(context.packageName)
            },
            onDismissRequest = {},
            content = {
                Text(
                    text = "'Acon'에 대한 위치접근 권한이 없습니다.",
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
            title = stringResource(R.string.location_certification_failure_dialog_title),
            action = "확인",
            onDismissRequest = {},
            onAction = { (context as? Activity)?.finishAffinity() },
            content = {
                Text(
                    text = "문제가 발생했습니다.\n나중에 다시 시도해주세요.",
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
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        AconTopBar(
            leadingIcon = {
                IconButton(onClick = onBackClick) {
                    Image(
                        imageVector = ImageVector.vectorResource(
                            id = com.acon.acon.core.designsystem.R.drawable.ic_left
                        ),
                        contentDescription = "Back"
                    )
                }
            },
            content = {
                Text(
                    text = "지역인증",
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
                            verifiedAreaId = state.verifiedAreaList[0].verifiedAreaId,
                            latitude = currentLatitude,
                            longitude = currentLongitude
                        )
                        onNavigateToNext()
                    } else {
                        viewModel.verifyArea(currentLatitude, currentLongitude)
                        onNavigateToNext()
                    }
                }
            )
        }
    }
}
