package com.acon.acon.feature.profile.composable.screen.profileMod

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.component.button.AconFilledLargeButton
import com.acon.acon.core.designsystem.component.dialog.AconTwoButtonDialog
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.utils.feature.permission.CheckAndRequestPhotoPermission
import com.acon.acon.feature.profile.R
import com.acon.acon.feature.profile.composable.component.CustomModalBottomSheet
import com.acon.acon.feature.profile.composable.component.NicknameErrMessageRow
import com.acon.acon.feature.profile.composable.component.ProfilePhotoBox
import com.acon.acon.feature.profile.composable.component.ProfileTextField
import com.acon.acon.feature.profile.composable.component.addFocusCleaner
import com.acon.acon.feature.profile.composable.type.BirthdayValidationStatus
import com.acon.acon.feature.profile.composable.type.FocusType
import com.acon.acon.feature.profile.composable.type.NicknameValidationStatus
import com.acon.acon.feature.profile.composable.utils.BirthdayTransformation
import com.acon.acon.feature.profile.composable.utils.limitedNicknameTextFieldValue

@Composable
internal fun ProfileModScreenV2(
    modifier: Modifier = Modifier,
    state: ProfileModStateV2,
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
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    val nickNameFocusRequester = remember { FocusRequester() }
    val birthDayFocusRequester = remember { FocusRequester() }

    BackHandler(enabled = true) {
        onRequestExitDialog()
    }

    when (state) {
        ProfileModStateV2.LoadFailed -> {}
        ProfileModStateV2.Loading -> {}
        is ProfileModStateV2.Success -> {
            var nicknameTextFieldValue by rememberSaveable(
                state.fetchedNickname,
                stateSaver = TextFieldValue.Saver
            ) {
                mutableStateOf(TextFieldValue(state.fetchedNickname))
            }

            val limitedTextFieldValue = remember(nicknameTextFieldValue) {
                nicknameTextFieldValue.limitedNicknameTextFieldValue()
            }

            val isProfileImageChanged =
                when {
                    state.selectedPhotoUri.contains("basic_profile_image") && state.fetchedPhotoUri.contains(
                        "basic_profile_image"
                    ) -> false

                    state.selectedPhotoUri.isNotEmpty() && state.selectedPhotoUri != state.fetchedPhotoUri -> true
                    else -> false
                }
            val isAnyFieldEdited = state.isEdited
            val isBirthdayRemoved =
                state.fetchedBirthday.isNotEmpty() && state.birthdayTextFieldValue.text.isEmpty()
            val isNicknameValid = state.nicknameValidationStatus == NicknameValidationStatus.Valid
            val isBirthdayValidOrEmpty =
                state.birthdayTextFieldValue.text.isEmpty() || state.birthdayValidationStatus == BirthdayValidationStatus.Valid
            val isEnabled =
                isProfileImageChanged || isBirthdayRemoved || (isAnyFieldEdited && isNicknameValid && isBirthdayValidOrEmpty)

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
                AconTwoButtonDialog(
                    title = stringResource(R.string.profile_mod_alert_title),
                    content = stringResource(R.string.profile_mod_alert_description),
                    leftButtonContent = stringResource(R.string.profile_mod_alert_left_btn),
                    rightButtonContent = stringResource(R.string.profile_mod_alert_right_btn),
                    contentImage = 0,
                    onDismissRequest = {
                        onRequestExitDialog()
                    },
                    onClickLeft = {
                        navigateToBack()
                    },
                    onClickRight = {
                        onDisMissExitDialog()
                    },
                    isImageEnabled = false
                )
            }

            if (state.showPermissionDialog) {
                AconTwoButtonDialog(
                    title = stringResource(R.string.photo_permission_alert_title),
                    content = stringResource(R.string.photo_permission_alert_subtitle),
                    leftButtonContent = stringResource(R.string.photo_permission_alert_left_btn),
                    rightButtonContent = stringResource(R.string.photo_permission_alert_right_btn),
                    contentImage = 0,
                    onDismissRequest = { onDisMissPermissionDialog() },
                    onClickLeft = { onDisMissPermissionDialog() },
                    onClickRight = { moveToSettings(context.packageName) },
                    isImageEnabled = false
                )
            }

            if (state.showPhotoEditModal) {
                CustomModalBottomSheet(
                    onDismiss = { onDisMissProfileEditModal() },
                    onGallerySelect = { onRequestPhotoPermission() },
                    onDefaultImageSelect = { onUpdateProfileImage("basic_profile_image") },
                )
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = AconTheme.color.Gray9)
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .addFocusCleaner(focusManager),
                verticalArrangement = Arrangement.Center
            ) {
                AconTopBar(
                    leadingIcon = {
                        IconButton(
                            onClick = onRequestExitDialog
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = com.acon.acon.core.designsystem.R.drawable.ic_arrow_left_28),
                                contentDescription = stringResource(R.string.content_description_back),
                            )
                        }
                    },
                    content = {
                        Text(
                            text = stringResource(R.string.profile_edit_topbar),
                            style = AconTheme.typography.head5_22_sb,
                            color = AconTheme.color.White
                        )
                    }
                )

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .weight(1f)
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
                                Icon(
                                    imageVector = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.and_ic_profile_img_edit),
                                    contentDescription = stringResource(R.string.content_description_edit_profile_icon),
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .align(alignment = Alignment.BottomEnd)
                                        .noRippleClickable { onProfileClicked() }
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = stringResource(R.string.nickname_textfield_title),
                                    style = AconTheme.typography.head8_16_sb,
                                    color = AconTheme.color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(R.string.star),
                                    style = AconTheme.typography.head8_16_sb,
                                    color = AconTheme.color.Main_org1
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            ProfileTextField(
                                status = state.nicknameFieldStatus,
                                focusType = FocusType.Nickname,
                                focusRequester = nickNameFocusRequester,
                                value = limitedTextFieldValue,
                                placeholder = stringResource(R.string.nickname_textfield_placeholder),
                                isTyping = (state.nicknameValidationStatus == NicknameValidationStatus.Typing),
                                onTextChanged = { fieldValue ->
                                    nicknameTextFieldValue = fieldValue
                                    onNicknameChanged(fieldValue.text)
                                },
                                onFocusChanged = onFocusChanged,
                                onClick = {
                                    nickNameFocusRequester.requestFocus()
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    when (val status = state.nicknameValidationStatus) {
                                        is NicknameValidationStatus.Empty -> {
                                            NicknameErrMessageRow(
                                                modifier = modifier,
                                                iconRes = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.and_ic_error_20),
                                                errMessage = stringResource(R.string.nickname_error_msg_input_nickname),
                                                textColor = AconTheme.color.Error_red1
                                            )
                                        }

                                        is NicknameValidationStatus.Error -> {
                                            status.errorTypes.forEach { error ->
                                                NicknameErrMessageRow(
                                                    modifier = modifier,
                                                    iconRes = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.and_ic_error_20),
                                                    errMessage = error.message,
                                                    textColor = AconTheme.color.Error_red1
                                                )
                                            }
                                        }

                                        is NicknameValidationStatus.Valid -> {
                                            NicknameErrMessageRow(
                                                modifier = modifier,
                                                iconRes = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.and_ic_local_check_mark_20),
                                                errMessage = stringResource(R.string.nickname_vaild_msg_available_nickname),
                                                textColor = AconTheme.color.Success_blue1
                                            )
                                        }

                                        else -> Unit
                                    }
                                }

                                Row(
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "${state.nicknameCount}",
                                        style = AconTheme.typography.subtitle2_14_med,
                                        color = AconTheme.color.White
                                    )
                                    Text(
                                        text = stringResource(R.string.nickname_max_nickname_length),
                                        style = AconTheme.typography.subtitle2_14_med,
                                        color = AconTheme.color.Gray5
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = stringResource(R.string.nickname_birthday_title),
                                    style = AconTheme.typography.head8_16_sb,
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
                                placeholder = stringResource(R.string.birthday_textfield_placeholder),
                                onTextChanged = { fieldValue ->
                                    onBirthdayChanged(fieldValue)
                                },
                                onFocusChanged = onFocusChanged,
                                visualTransformation = BirthdayTransformation(),
                                onClick = {
                                    birthDayFocusRequester.requestFocus()
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            when (val status = state.birthdayValidationStatus) {
                                is BirthdayValidationStatus.Valid -> {
                                    Spacer(modifier = Modifier.height(4.dp))
                                }

                                is BirthdayValidationStatus.Invalid -> {
                                    NicknameErrMessageRow(
                                        modifier = modifier,
                                        iconRes = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.and_ic_error_20),
                                        errMessage = status.errorMsg ?: "",
                                        textColor = AconTheme.color.Error_red1
                                    )
                                }

                                else -> Spacer(modifier = Modifier.height(4.dp))
                            }
                        }

                    }
                }

                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 10.dp)
                ) {
                    AconFilledLargeButton(
                        text = stringResource(R.string.save_btn),
                        textStyle = AconTheme.typography.head8_16_sb,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        disabledBackgroundColor = AconTheme.color.Gray7,
                        enabledBackgroundColor = AconTheme.color.Gray5,
                        disabledTextColor = AconTheme.color.Gray5,
                        enabledTextColor = AconTheme.color.White,
                        onClick = { onClickSaveButton(limitedTextFieldValue.text) },
                        isEnabled = isEnabled
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun ProfileModScreenPreview() {
    AconTheme {
        ProfileModScreenV2(
            modifier = Modifier,
            state = ProfileModStateV2.Success(),
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
            onClickSaveButton = {},
        )
    }
}