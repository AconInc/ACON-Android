package com.acon.acon.feature.profile.composable.screen.profileMod.composable

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.component.dialog.v2.AconTwoActionDialog
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.utils.feature.permission.CheckAndRequestPhotoPermission
import com.acon.acon.feature.profile.composable.component.GallerySelectBottomSheet
import com.acon.acon.feature.profile.composable.component.NicknameValidMessageRow
import com.acon.acon.feature.profile.composable.component.ProfilePhotoBox
import com.acon.acon.feature.profile.composable.component.ProfileTextField
import com.acon.acon.feature.profile.composable.component.addFocusCleaner
import com.acon.acon.feature.profile.composable.screen.profileMod.ProfileModState
import com.acon.acon.feature.profile.composable.type.BirthdayValidationStatus
import com.acon.acon.feature.profile.composable.type.FocusType
import com.acon.acon.feature.profile.composable.type.NicknameValidationStatus
import com.acon.acon.feature.profile.composable.type.contentDescriptionResId
import com.acon.acon.feature.profile.composable.type.validMessageResId
import com.acon.acon.feature.profile.composable.utils.BirthdayTransformation
import com.acon.acon.feature.profile.composable.utils.limitedNicknameTextFieldValue
import dev.chrisbanes.haze.hazeSource

@Composable
internal fun ProfileModScreen(
    modifier: Modifier = Modifier,
    state: ProfileModState,
    navigateToBack: () -> Unit,
    navigateToCustomGallery: () -> Unit,
    onNicknameChanged: (String) -> Unit = {},
    onBirthdayChanged: (TextFieldValue) -> Unit = {},
    onFocusChanged: (Boolean, FocusType) -> Unit = { _, _ -> },
    onRequestExitDialog: () -> Unit,
    onDisMissExitDialog: () -> Unit,
    onRequestPhotoPermission: () -> Unit,
    onDisMissPhotoPermission: () -> Unit,
    onRequestPermissionDialog: () -> Unit,
    onDisMissPermissionDialog: () -> Unit,
    moveToSettings: (String) -> Unit,
    onProfileClicked: () -> Unit = {},
    onDisMissProfileEditModal: () -> Unit,
    onUpdateProfileImage: (String) -> Unit,
    onClickSaveButton: (String) -> Unit = {},
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    val boxHeight = (screenHeightDp * (80f / 740f))

    val focusManager = LocalFocusManager.current

    val nickNameFocusRequester = remember { FocusRequester() }
    val birthDayFocusRequester = remember { FocusRequester() }

    BackHandler(enabled = true) {
        onRequestExitDialog()
    }

    when (state) {
        ProfileModState.LoadFailed -> {}
        ProfileModState.Loading -> {}
        is ProfileModState.Success -> {
            var nicknameTextFieldValue by rememberSaveable(
                state.fetchedNickname,
                stateSaver = TextFieldValue.Saver
            ) {
                mutableStateOf(TextFieldValue(state.fetchedNickname))
            }

            val limitedTextFieldValue = remember(nicknameTextFieldValue) {
                nicknameTextFieldValue.limitedNicknameTextFieldValue()
            }

            if (state.requestPhotoPermission) {
                CheckAndRequestPhotoPermission(
                    onPermissionGranted = {
                        onDisMissPhotoPermission()
                        navigateToCustomGallery()
                    },
                    onPermissionDenied = {
                        onRequestPermissionDialog()
                        onRequestPhotoPermission()
                    }
                )
            }

            if (state.showExitDialog) {
                AconTwoActionDialog(
                    title = stringResource(R.string.profile_mod_exit_title),
                    action1 = stringResource(R.string.continue_writing),
                    action2 = stringResource(R.string.exit),
                    onDismissRequest = {
                        onRequestExitDialog()
                    },
                    onAction1 = {
                        onDisMissExitDialog()
                    },
                    onAction2 = {
                        navigateToBack()
                    }
                )
            }

            if (state.showPermissionDialog) {
                AconTwoActionDialog(
                    title = stringResource(R.string.photo_permission_title),
                    action1 = stringResource(R.string.photo_permission_alert_left_btn),
                    action2 = stringResource(R.string.photo_permission_alert_right_btn),
                    onDismissRequest = { onDisMissPermissionDialog() },
                    onAction1 = { onDisMissPermissionDialog() },
                    onAction2 = { moveToSettings(context.packageName) },
                ) {
                    Text(
                        text = stringResource(R.string.photo_permission_content),
                        color = AconTheme.color.Gray200,
                        style = AconTheme.typography.Body1,
                        maxLines = 1,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }
            }

            if (state.showPhotoEditModal) {
                GallerySelectBottomSheet(
                    isDefault = state.fetchedPhotoUri.contains("basic_profile_image"),
                    onDismiss = { onDisMissProfileEditModal() },
                    onGallerySelect = { onRequestPhotoPermission() },
                    onDefaultImageSelect = { onUpdateProfileImage("basic_profile_image") },
                )
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = AconTheme.color.Gray900)
                    .hazeSource(LocalHazeState.current)
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .addFocusCleaner(focusManager),
                verticalArrangement = Arrangement.Center
            ) {
                AconTopBar(
                    modifier = Modifier.padding(vertical = 14.dp),
                    leadingIcon = {
                        IconButton(
                            onClick = onRequestExitDialog
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_topbar_arrow_left),
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    },
                    content = {
                        Text(
                            text = stringResource(R.string.profile_edit_topbar),
                            style = AconTheme.typography.Title4,
                            fontWeight = FontWeight.SemiBold,
                            color = AconTheme.color.White
                        )
                    }
                )

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 19.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .size(boxHeight.dp)
                                    .aspectRatio(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                ProfilePhotoBox(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center),
                                    onProfileClicked = onProfileClicked,
                                    photoUri = state.selectedPhotoUri.ifEmpty { state.fetchedPhotoUri }
                                )
                                Image(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_profile_img_edit),
                                    contentDescription = stringResource(R.string.content_description_edit_profile),
                                    modifier = Modifier
                                        .align(alignment = Alignment.BottomEnd)
                                        .noRippleClickable { onProfileClicked() }
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 48.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = stringResource(R.string.nickname_textfield_title),
                                    style = AconTheme.typography.Title4,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AconTheme.color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(R.string.star),
                                    style = AconTheme.typography.Title4,
                                    color = AconTheme.color.Gray50
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            ProfileTextField(
                                status = state.nicknameFieldStatus,
                                focusType = FocusType.Nickname,
                                focusRequester = nickNameFocusRequester,
                                value = limitedTextFieldValue,
                                placeholder = stringResource(R.string.nickname_placeholder),
                                isTyping = (state.nicknameValidationStatus == NicknameValidationStatus.Typing),
                                onValueChange = { fieldValue ->
                                    nicknameTextFieldValue = fieldValue
                                    onNicknameChanged(fieldValue.text)
                                },
                                onFocusChanged = onFocusChanged,
                                onClick = {
                                    nickNameFocusRequester.requestFocus()
                                }
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    when (state.nicknameValidationStatus) {
                                        is NicknameValidationStatus.Empty -> {
                                            NicknameValidMessageRow(
                                                validMessage = R.string.nickname_error_empty,
                                                iconRes = R.drawable.ic_error,
                                                validContentDescription = R.string.content_description_empty_nickname,
                                                color = AconTheme.color.Danger
                                            )
                                        }

                                        is NicknameValidationStatus.Error -> {
                                            val validState = state.nicknameValidationStatus
                                            NicknameValidMessageRow(
                                                validMessage = validState.errorTypes.validMessageResId(),
                                                iconRes = R.drawable.ic_error,
                                                validContentDescription = validState.errorTypes.contentDescriptionResId(),
                                                color = AconTheme.color.Danger
                                            )
                                        }

                                        is NicknameValidationStatus.Valid -> {
                                            NicknameValidMessageRow(
                                                validMessage = R.string.nickname_valid,
                                                iconRes = R.drawable.ic_valid,
                                                validContentDescription = R.string.content_description_valid_nickname,
                                                color = AconTheme.color.Success
                                            )
                                        }

                                        else -> Unit
                                    }
                                }
                            }

                            Text(
                                text = stringResource(R.string.nickname_textfield_guide),
                                color = AconTheme.color.Gray300,
                                style = AconTheme.typography.Body1,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 44.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = stringResource(R.string.nickname_birthday_title),
                                    style = AconTheme.typography.Title4,
                                    color = AconTheme.color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            ProfileTextField(
                                status = state.birthdayFieldStatus,
                                focusType = FocusType.Birthday,
                                focusRequester = birthDayFocusRequester,
                                value = state.birthdayTextFieldValue,
                                placeholder = stringResource(R.string.birthday_placeholder),
                                onValueChange = { fieldValue ->
                                    onBirthdayChanged(fieldValue)
                                },
                                onFocusChanged = onFocusChanged,
                                visualTransformation = BirthdayTransformation(),
                                onClick = {
                                    birthDayFocusRequester.requestFocus()
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            when (state.birthdayValidationStatus) {
                                is BirthdayValidationStatus.Valid -> {
                                    Spacer(modifier = Modifier.height(4.dp))
                                }

                                is BirthdayValidationStatus.Invalid -> {
                                    NicknameValidMessageRow(
                                        validMessage = R.string.birthday_error_invalid,
                                        iconRes = R.drawable.ic_error,
                                        validContentDescription = R.string.content_description_invalid_birthday,
                                        color = AconTheme.color.Danger
                                    )
                                }

                                else -> Spacer(modifier = Modifier.height(4.dp))
                            }
                        }

                        Spacer(Modifier.weight(1f))
                        AconFilledButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 16.dp),
                            enabled = state.isEditButtonEnabled,
                            onClick = { onClickSaveButton(limitedTextFieldValue.text) },
                            content = {
                                Text(
                                    text = stringResource(R.string.save),
                                    style = AconTheme.typography.Title4,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        )
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun ProfileModScreenPreview() {
    AconTheme {
        ProfileModScreen(
            modifier = Modifier,
            state = ProfileModState.Success(),
            navigateToBack = {},
            navigateToCustomGallery = {},
            onNicknameChanged = {},
            onBirthdayChanged = {},
            onFocusChanged = { _, _ -> },
            onRequestExitDialog = {},
            onDisMissExitDialog = {},
            onRequestPhotoPermission = {},
            onDisMissPhotoPermission = {},
            onRequestPermissionDialog = {},
            onDisMissPermissionDialog = {},
            moveToSettings = {},
            onProfileClicked = {},
            onDisMissProfileEditModal = {},
            onUpdateProfileImage = {},
            onClickSaveButton = {}
        )
    }
}