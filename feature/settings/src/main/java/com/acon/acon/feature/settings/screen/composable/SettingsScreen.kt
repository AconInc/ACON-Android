package com.acon.acon.feature.settings.screen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.dialog.v2.AconTwoActionDialog
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.effect.rememberHazeState
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.settings.component.SettingSectionItem
import com.acon.acon.feature.settings.component.SettingSectionVersionItem
import com.acon.acon.feature.settings.screen.SettingsUiState
import com.acon.acon.feature.settings.type.SettingsType
import com.acon.core.analytics.amplitude.AconAmplitude
import com.acon.core.analytics.constants.EventNames
import com.acon.core.analytics.constants.PropertyKeys
import com.acon.core.ui.compose.getScreenWidth
import dev.chrisbanes.haze.hazeSource

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    isLatestVersion: Boolean,
    modifier: Modifier = Modifier,
    onLogoutDialogShowStateChange: (Boolean) -> Unit = {},
    navigateBack: () -> Unit = {},
    onTermOfUse: () -> Unit = {},
    onPrivatePolicy: () -> Unit = {},
    onRetryOnBoarding: () -> Unit = {},
    onAreaVerification: () -> Unit = {},
    onUpdateVersion: () -> Unit = {},
    onSignOut: () -> Unit = {},
    onDeleteAccountScreen: () -> Unit = {},
) {
    val screenWidthDp = getScreenWidth()
    val dialogWidth = (screenWidthDp *  (260f / 360f))

    val hazeState = rememberHazeState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .background(AconTheme.color.Gray900)
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .hazeSource(hazeState)
    ) {
        when (state) {
            is SettingsUiState.User -> {
                if (state.showLogOutDialog) {
                    AconTwoActionDialog(
                        title = stringResource(R.string.logout_dialog_title),
                        action1 = stringResource(R.string.cancel),
                        action2 = stringResource(R.string.settings_section_logout),
                        onDismissRequest = { onLogoutDialogShowStateChange(false) },
                        onAction1 = { onLogoutDialogShowStateChange(false) },
                        onAction2 = {
                            AconAmplitude.trackEvent(
                                eventName = EventNames.SERVICE_WITHDRAW,
                                property = PropertyKeys.CLICK_SIGN_OUT to true
                            )
                            onSignOut()
                        },
                        modifier = Modifier.width(dialogWidth)
                    )
                }

                AconTopBar(
                    leadingIcon = {
                        IconButton(
                            onClick = { navigateBack() }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_topbar_arrow_left),
                                contentDescription = stringResource(R.string.back),
                                tint = AconTheme.color.Gray50
                            )
                        }
                    },
                    content = {
                        Text(
                            text = stringResource(R.string.settings_screen_topbar),
                            style = AconTheme.typography.Title4,
                            fontWeight = FontWeight.SemiBold,
                            color = AconTheme.color.White
                        )
                    },
                    modifier = Modifier.padding(vertical = 14.dp)
                )

                Column(
                    modifier = modifier.padding(horizontal = 16.dp)
                ) {
                    Spacer(Modifier.height(32.dp))
                    Text(
                        text = stringResource(R.string.settings_title_version_info),
                        style = AconTheme.typography.Body1,
                        color = AconTheme.color.Gray500
                    )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionVersionItem(
                        isLatestVersion = isLatestVersion, // TODO - 버전 분기 처리
                        onClickContinue = onUpdateVersion
                    )

                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = stringResource(R.string.settings_title_terms_and_policies),
                        style = AconTheme.typography.Body1,
                        color = AconTheme.color.Gray500
                    )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionItem(
                        settingsType = SettingsType.TERM_OF_USE,
                        onClickContinue = onTermOfUse
                    )

                    Spacer(Modifier.height(12.dp))
                    SettingSectionItem(
                        settingsType = SettingsType.PRIVACY_POLICY,
                        onClickContinue = onPrivatePolicy
                    )

                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = stringResource(R.string.settings_title_service_settings),
                        style = AconTheme.typography.Body1,
                        color = AconTheme.color.Gray500,
                    )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionItem(
                        settingsType = SettingsType.ONBOARDING_AGAIN,
                        onClickContinue = onRetryOnBoarding,
                    )

                    Spacer(Modifier.height(12.dp))
                    SettingSectionItem(
                        settingsType = SettingsType.AREA_VERIFICATION,
                        onClickContinue = onAreaVerification,
                    )

                Spacer(Modifier.height(40.dp))
                Text(
                    text = stringResource(R.string.settings_title_sign_in_and_delete_account),
                    style = AconTheme.typography.Body1,
                    color = AconTheme.color.Gray500
                )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionItem(
                        settingsType = SettingsType.LOGOUT,
                        onClickContinue = { onLogoutDialogShowStateChange(true) }
                    )

                    Spacer(Modifier.height(12.dp))
                    SettingSectionItem(
                        settingsType = SettingsType.DELETE_ACCOUNT,
                        onClickContinue = {
                            AconAmplitude.trackEvent(
                                eventName = EventNames.SERVICE_WITHDRAW,
                                property = PropertyKeys.CLICK_EXIT_SERVICE to true
                            )
                            onDeleteAccountScreen()
                        }
                    )
                }
            }

            is SettingsUiState.Guest -> {
                AconTopBar(
                    leadingIcon = {
                        IconButton(
                            onClick = { navigateBack() }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_topbar_arrow_left),
                                contentDescription = stringResource(R.string.back),
                                tint = AconTheme.color.Gray50
                            )
                        }
                    },
                    content = {
                        Text(
                            text = stringResource(R.string.settings_screen_topbar),
                            style = AconTheme.typography.Title4,
                            fontWeight = FontWeight.SemiBold,
                            color = AconTheme.color.White
                        )
                    },
                    modifier = Modifier.padding(vertical = 14.dp)
                )

                Column(
                    modifier = modifier.padding(horizontal = 16.dp)
                ) {
                    Spacer(Modifier.height(32.dp))
                    Text(
                        text = stringResource(R.string.settings_title_version_info),
                        style = AconTheme.typography.Body1,
                        color = AconTheme.color.Gray500
                    )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionVersionItem(
                        isLatestVersion = isLatestVersion, // TODO - 버전 분기 처리
                        onClickContinue = onUpdateVersion
                    )

                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = stringResource(R.string.settings_title_terms_and_policies),
                        style = AconTheme.typography.Body1,
                        color = AconTheme.color.Gray500
                    )

                    Spacer(Modifier.height(16.dp))
                    SettingSectionItem(
                        settingsType = SettingsType.TERM_OF_USE,
                        onClickContinue = onTermOfUse
                    )

                    Spacer(Modifier.height(12.dp))
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
            state = SettingsUiState.User(),
            isLatestVersion = true
        )
    }
}