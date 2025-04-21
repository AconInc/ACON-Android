package com.acon.acon.feature.areaverification

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.designsystem.blur.LocalHazeState
import com.acon.acon.core.designsystem.component.button.AconFilledLargeButton
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.utils.feature.action.BackOnPressed
import com.acon.acon.core.designsystem.component.dialog.AconPermissionDialog
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.utils.feature.permission.CheckAndRequestLocationPermission
import com.acon.acon.core.utils.feature.permission.checkLocationPermission
import com.acon.acon.feature.areaverification.component.AreaVerificationButton
import dev.chrisbanes.haze.hazeSource

@Composable
fun AreaVerificationScreenContainer(
    route: String,
    onNewAreaClick: (Double, Double) -> Unit,
    onNextScreen: (Double, Double) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AreaVerificationViewModel = hiltViewModel(),
    onNavigateBack:() -> Unit = {}
) {
    val context = LocalContext.current
    val state by viewModel.container.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { effect ->
            when (effect) {
                is AreaVerificationSideEffect.NavigateToSettings -> {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", effect.packageName, null)
                    }
                    context.startActivity(intent)
                }

                is AreaVerificationSideEffect.NavigateToGPSSettings -> {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
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

                is AreaVerificationSideEffect.OnNavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    CheckAndRequestLocationPermission(showDialogAutomatically = false) {}

    AreaVerificationScreen(
        state = state,
        route = route,
        onNewLocationSelected = { viewModel.onNewLocationSelected() },
        onPermissionSettingClick = { viewModel.onPermissionSettingClick(context.packageName) },
        onNextButtonClick = {
            val hasPermission = context.checkLocationPermission()
            viewModel.updateLocationPermissionStatus(hasPermission)

            if (hasPermission) { viewModel.onNextButtonClick() }
            else { viewModel.updateShowPermissionDialog(true) }
        },
        modifier = modifier,
        onBackClick = onNavigateBack
    )
}

@Composable
fun AreaVerificationScreen(
    state: AreaVerificationState,
    route: String,
    onNewLocationSelected: () -> Unit,
    onPermissionSettingClick: () -> Unit,
    onNextButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    onBackClick:() -> Unit = {}
) {
    val context = LocalContext.current

    if (state.showPermissionDialog) {
        AconPermissionDialog(
            onPermissionGranted = onPermissionSettingClick
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AconTheme.color.Gray9)
            .hazeSource(LocalHazeState.current),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (route == stringResource(R.string.area_verification_settings)) {
                Spacer(Modifier.height(42.dp))
                AconTopBar(
                    modifier = Modifier
                        .padding(vertical = 14.dp, horizontal = 10.dp),
                    paddingValues = PaddingValues(horizontal = 0.dp),
                    leadingIcon = {
                        IconButton(onClick = onBackClick) {
                            Image(
                                imageVector = ImageVector.vectorResource(
                                    id = com.acon.acon.core.designsystem.R.drawable.ic_arrow_left_28
                                ),
                                contentDescription = stringResource(R.string.back_btn),
                            )
                        }
                    },
                )
                Spacer(modifier = Modifier.height(32.dp))
            } else {
                BackOnPressed(context)
                Spacer(modifier = Modifier.height(132.dp))
            }

            Text(
                text = stringResource(R.string.local_restaurant_certification_title),
                style = AconTheme.typography.head5_22_sb,
                color = AconTheme.color.Gray1,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.local_restaurant_certification_description),
                style = AconTheme.typography.subtitle2_14_med,
                color = AconTheme.color.Gray3,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
            )

            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
                .padding(horizontal = 16.dp)
                .padding(bottom = 40.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAreaVerificationScreen() {
    val sampleState = AreaVerificationState(
        showPermissionDialog = false,
        isNewLocationSelected = true
    )
    AreaVerificationScreen(
        route = "settings",
        state = sampleState,
        onNewLocationSelected = {},
        onPermissionSettingClick = {},
        onNextButtonClick = {}
    )
}
