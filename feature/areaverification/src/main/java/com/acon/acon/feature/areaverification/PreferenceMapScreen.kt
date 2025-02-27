package com.acon.acon.feature.areaverification

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acon.acon.core.designsystem.blur.LocalHazeState
import com.acon.acon.core.designsystem.component.button.AconFilledLargeButton
import com.acon.acon.core.designsystem.component.dialog.AconOneButtonDialog
import com.acon.acon.core.designsystem.component.dialog.AconTwoButtonDialog
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.areaverification.component.DottoriSelectionBottomSheet
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferenceMapScreen(
    latitude: Double,
    longitude: Double,
    isEdit: Boolean,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    onNavigateToNext: () -> Unit = {},
    viewModel: AreaVerificationViewModel = hiltViewModel()
) {
    var currentLatitude by remember { mutableDoubleStateOf(latitude) }
    var currentLongitude by remember { mutableDoubleStateOf(longitude) }

    val context = LocalContext.current
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.fetchVerifiedArea()
        viewModel.checkGPSStatus()
        viewModel.checkSupportLocation(context)

        viewModel.container.sideEffectFlow.collect { effect ->
            when (effect) {
                is AreaVerificationSideEffect.NavigateToGPSSettings -> {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(intent)
                }
                else -> {

                }
            }
        }
    }

    LaunchedEffect(state.isGPSEnabled){
        //viewModel.fetchVerifiedArea()
        viewModel.checkGPSStatus()
        viewModel.checkSupportLocation(context)
    }

    if (state.verifiedArea != null) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )

        ModalBottomSheet(
            onDismissRequest = { viewModel.resetVerifiedArea() },
            containerColor = Color.Transparent,
            dragHandle = null,
            sheetState = sheetState,
        ) {
            DottoriSelectionBottomSheet(
                onDismiss = { viewModel.resetVerifiedArea() },
                onNavigateToNext = {
                    amplitudeClickGoHome()
                    scope.launch {
                        sheetState.hide()
                        viewModel.resetVerifiedArea()
                        onNavigateToNext()
                    }
                },
                sheetState = sheetState
            )
        }
    }

    if (state.showGPSDialog){
        AconTwoButtonDialog(
            title = stringResource(R.string.check_gps_title_2),
            content = stringResource(R.string.check_gps_content_2),
            leftButtonContent = stringResource(R.string.check_gps_cancel_btn),
            rightButtonContent = stringResource(R.string.check_gps_setting_btn),
            contentImage = 0,
            onDismissRequest = {},
            onClickLeft = { // 다이얼로그 숨기기
                viewModel.hideGPSDialog()
            },
            onClickRight = { //설정으로 바로 이동시키기
                viewModel.onGPSSettingClick(context.packageName)
            },
            isImageEnabled = false
        )
    }

    if (state.showLocationDialog){
        AconOneButtonDialog(
            title = "위치 인증 실패",
            content = "현재 인증이 불가능한 지역에 있어요.",
            buttonContent = "확인",
            contentImage = com.acon.acon.core.designsystem.R.drawable.ic_error_2_120,
            onDismissRequest = {},
            onClickConfirm = { viewModel.hideLocationDialog() },
            imageSize = 104.dp,
            isImageEnabled = true
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AconTheme.color.Gray9)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        AconTopBar(
            leadingIcon = {
                IconButton(onClick = onBackClick) {
                    Image(
                        imageVector = ImageVector.vectorResource(
                            id = com.acon.acon.core.designsystem.R.drawable.ic_arrow_left_28
                        ),
                        contentDescription = "Back",
                    )
                }
            },
            content = {
                Text(
                    text = stringResource(R.string.check_location_on_map),
                    style = AconTheme.typography.head5_22_sb,
                    color = AconTheme.color.White
                )
            },
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .hazeSource(LocalHazeState.current)
                .fillMaxWidth()
        ) {
            LocationMapScreen(
                onLocationObtained = { lat, lng ->
                    currentLatitude = lat
                    currentLongitude = lng
                },
                initialLatitude = latitude,
                initialLongitude = longitude,
                modifier = Modifier.fillMaxSize()
            )
        }

        AconFilledLargeButton(
            text = stringResource(R.string.verify_complete),
            textStyle = AconTheme.typography.head8_16_sb,
            enabledBackgroundColor = AconTheme.color.Gray5,
            disabledBackgroundColor = AconTheme.color.Gray8,
            enabledTextColor = AconTheme.color.White,
            onClick = {
                if (isEdit) {
                    viewModel.editVerifiedArea(
                        verifiedAreaId = state.verifiedAreaList[0].verifiedAreaId,
                        latitude = currentLatitude,
                        longitude = currentLongitude
                    )
                } else {
                    viewModel.verifyArea(currentLatitude, currentLongitude)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 40.dp)
        )
    }
}
