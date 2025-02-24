package com.acon.acon.feature.settings.screen.composable

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.component.dialog.AconTwoButtonDialog
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.settings.component.SettingSectionItem
import com.acon.acon.feature.settings.component.SettingSectionVersionItem
import com.acon.acon.feature.settings.screen.SettingsUiState
import com.acon.acon.feature.settings.type.SettingsType
import com.acon.acon.feature.settings.R

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    versionName: String,
    modifier: Modifier = Modifier,
    onSignInDialogShowStateChange: (Boolean) -> Unit = {},
    navigateBack: () -> Unit = {},
    onTermOfUse: () -> Unit = {},
    onPrivatePolicy: () -> Unit = {},
    onRetryOnBoarding: () -> Unit = {},
    onAreaVerification: () -> Unit = {},
    onUpdateVersion: () -> Unit = {},
    onSignOut: () -> Unit = {},
    onDeleteAccountScreen: () -> Unit = {},
) {
    BackHandler {
        navigateBack()
    }

    when(state) {
        is SettingsUiState.Default -> {
            if(state.onSignInDialogShowStateChange) {
                AconTwoButtonDialog(
                    title = stringResource(R.string.logout_dialog_title),
                    content = stringResource(R.string.logout_dialog_content),
                    leftButtonContent = stringResource(R.string.logout_dialog_cancel_btn),
                    rightButtonContent = stringResource(R.string.settings_section_logout),
                    onDismissRequest = { onSignInDialogShowStateChange(false) },
                    onClickLeft = { onSignInDialogShowStateChange(false) },
                    onClickRight =  onSignOut,
                    isImageEnabled = false
                )
            }

            Column(
                modifier = modifier
                    .background(AconTheme.color.Gray9)
                    .padding(horizontal = 16.dp)
            ) {
                if(state.isLogin) {
                    Spacer(Modifier.height(42.dp))
                    AconTopBar(
                        modifier = Modifier
                            .padding(vertical = 14.dp),
                        paddingValues = PaddingValues(0.dp),
                        leadingIcon = {
                            IconButton(
                                onClick = navigateBack
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.ic_arrow_left_28),
                                    contentDescription = stringResource(R.string.go_back_content_description),
                                    tint = AconTheme.color.White
                                )
                            }
                        },
                        content = {
                            Text(
                                text = stringResource(R.string.settings_screen_topbar),
                                style = AconTheme.typography.head5_22_sb,
                                color = AconTheme.color.White
                            )
                        },
                    )

                    Text(
                        text = stringResource(R.string.settings_title_version_info),
                        style = AconTheme.typography.subtitle2_14_med,
                        color = AconTheme.color.Gray5
                    )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionVersionItem(
                        currentVersion = versionName, // TODO - 앱 출시 후 버전 분기 처리
                        onClickContinue = onUpdateVersion
                    )

                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = stringResource(R.string.settings_title_terms_and_policies),
                        style = AconTheme.typography.subtitle2_14_med,
                        color = AconTheme.color.Gray5
                    )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionItem(
                        settingsType = SettingsType.TERM_OF_USE,
                        onClickContinue = onTermOfUse
                    )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionItem(
                        settingsType = SettingsType.PRIVACY_POLICY,
                        onClickContinue = onPrivatePolicy
                    )

                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = stringResource(R.string.settings_title_service_settings),
                        style = AconTheme.typography.subtitle2_14_med,
                        color = AconTheme.color.Gray5
                    )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionItem(
                        settingsType = SettingsType.ONBOARDING_AGAIN,
                        onClickContinue = onRetryOnBoarding
                    )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionItem(
                        settingsType = SettingsType.AREA_VERIFICATION,
                        onClickContinue = onAreaVerification
                    )

                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = stringResource(R.string.settings_title_login_and_delete_account),
                        style = AconTheme.typography.subtitle2_14_med,
                        color = AconTheme.color.Gray5
                    )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionItem(
                        settingsType = SettingsType.LOGOUT,
                        onClickContinue = { onSignInDialogShowStateChange(true) }
                    )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionItem(
                        settingsType = SettingsType.DELETE_ACCOUNT,
                        onClickContinue = onDeleteAccountScreen
                    )
                }

                else {
                    Spacer(Modifier.height(42.dp))
                    AconTopBar(
                        modifier = Modifier
                            .padding(vertical = 14.dp),
                        paddingValues = PaddingValues(0.dp),
                        leadingIcon = {
                            IconButton(
                                onClick = navigateBack
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.ic_arrow_left_28),
                                    contentDescription = stringResource(R.string.go_back_content_description),
                                    tint = AconTheme.color.White
                                )
                            }
                        },
                        content = {
                            Text(
                                text = stringResource(R.string.settings_screen_topbar),
                                style = AconTheme.typography.head5_22_sb,
                                color = AconTheme.color.White
                            )
                        },
                    )

                    Text(
                        text = stringResource(R.string.settings_title_version_info),
                        style = AconTheme.typography.subtitle2_14_med,
                        color = AconTheme.color.Gray5
                    )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionVersionItem(
                        currentVersion = versionName,
                        onClickContinue = onUpdateVersion
                    )

                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = stringResource(R.string.settings_title_terms_and_policies),
                        style = AconTheme.typography.subtitle2_14_med,
                        color = AconTheme.color.Gray5
                    )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionItem(
                        settingsType = SettingsType.TERM_OF_USE,
                        onClickContinue = onTermOfUse
                    )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionItem(
                        settingsType = SettingsType.PRIVACY_POLICY,
                        onClickContinue = onPrivatePolicy
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun SettingsScreenPreview() {
    AconTheme {
        SettingsScreen(
            state = SettingsUiState.Default(
                isLogin = true
            ),
            versionName = ""
        )
    }
}