package com.acon.acon.feature.profile.composable.screen.profileMod.composable

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.acon.acon.core.ui.compose.getScreenHeight
import com.acon.acon.core.ui.compose.getScreenWidth
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
import dev.chrisbanes.haze.hazeSource

@Composable
internal fun ProfileModScreen(
    modifier: Modifier = Modifier,
    state: ProfileModState,
    navigateToBack: () -> Unit,
    onNicknameChanged: (String) -> Unit = {},
    onBirthdayChanged: (String) -> Unit = {},
    onFocusChanged: (Boolean, FocusType) -> Unit = { _, _ -> },
    onRequestExitDialog: () -> Unit,
    onDisMissExitDialog: () -> Unit,
    onRequestProfileEditModal: () -> Unit,
    onDisMissProfileEditModal: () -> Unit,
    onUpdateProfileImage: (String) -> Unit,
    onClickSaveButton: () -> Unit = {},
) {
    val screenWidthDp = getScreenWidth()
    val screenHeightDp = getScreenHeight()
    val dialogWidth = (screenWidthDp * (260f / 360f))
    val profileImageHeight = (screenHeightDp * (80f / 740f))

    val focusManager = LocalFocusManager.current

    val nickNameFocusRequester = remember { FocusRequester() }
    val birthDayFocusRequester = remember { FocusRequester() }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            onUpdateProfileImage(uri.toString())
            onDisMissProfileEditModal()
        }
    }

    when (state) {
        ProfileModState.LoadFailed -> {}
        ProfileModState.Loading -> {}
        is ProfileModState.Success -> {

            BackHandler(enabled = true) {
                if(state.isEdited) {
                    onRequestExitDialog()
                } else {
                    navigateToBack()
                }
            }

            var nicknameTextFieldValue by rememberSaveable(
                state.fetchedNickname,
                stateSaver = TextFieldValue.Saver
            ) {
                mutableStateOf(TextFieldValue(state.fetchedNickname))
            }

            var birthdayTextFieldValue by rememberSaveable(
                state.fetchedBirthday,
                stateSaver = TextFieldValue.Saver
            ) {
                mutableStateOf(TextFieldValue(state.fetchedBirthday))
            }

            if (state.showPhotoEditModal) {
                GallerySelectBottomSheet(
                    isDefault = when {
                        state.selectedPhotoUri.isNotEmpty() -> {
                            state.selectedPhotoUri.contains("basic_profile_image")
                        }

                        else -> {
                            state.fetchedPhotoUri.contains("basic_profile_image")
                        }
                    },
                    onDismiss = { onDisMissProfileEditModal() },
                    onGallerySelect = {
                        onDisMissProfileEditModal()
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
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
                    },
                    modifier = Modifier.width(dialogWidth)
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
                    leadingIcon = {
                        IconButton(
                            onClick = {
                                if(state.isEdited) { onRequestExitDialog() }
                                else { navigateToBack() }
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_topbar_arrow_left),
                                contentDescription = stringResource(R.string.back),
                                tint = AconTheme.color.Gray50
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
                    },
                    modifier = Modifier.padding(vertical = 14.dp)
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
                                    .size(profileImageHeight)
                                    .aspectRatio(1f)
                                    .noRippleClickable { onRequestProfileEditModal() },
                                contentAlignment = Alignment.Center
                            ) {
                                ProfilePhotoBox(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center),
                                    photoUri = state.selectedPhotoUri.ifEmpty { state.fetchedPhotoUri }
                                )
                                Image(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_profile_img_edit),
                                    contentDescription = stringResource(R.string.content_description_edit_profile),
                                    modifier = Modifier
                                        .align(alignment = Alignment.BottomEnd)
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
                                    fontWeight = FontWeight.SemiBold,
                                    color = AconTheme.color.Gray50
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            ProfileTextField(
                                status = state.nicknameFieldStatus,
                                focusType = FocusType.Nickname,
                                focusRequester = nickNameFocusRequester,
                                value = nicknameTextFieldValue,
                                isTyping = (state.nicknameValidationStatus == NicknameValidationStatus.Typing),
                                onValueChange = { fieldValue ->
                                    val lowerCaseText = fieldValue.text.lowercase()
                                    if (lowerCaseText.length <= 14) {
                                        nicknameTextFieldValue =
                                            fieldValue.copy(text = lowerCaseText)
                                        onNicknameChanged(lowerCaseText)
                                    }
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
                                    Box(
                                        Modifier.height(24.dp)
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

                                Row(
                                    modifier = Modifier.padding(top = 5.dp, end = 8.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "${state.nicknameCount}",
                                        style = AconTheme.typography.Caption1,
                                        color = AconTheme.color.Gray500
                                    )
                                    Text(
                                        text = stringResource(R.string.max_nickname_count),
                                        style = AconTheme.typography.Caption1,
                                        color = AconTheme.color.Gray500
                                    )
                                }
                            }
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
                                    color = AconTheme.color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            ProfileTextField(
                                status = state.birthdayFieldStatus,
                                focusType = FocusType.Birthday,
                                focusRequester = birthDayFocusRequester,
                                value = birthdayTextFieldValue,
                                placeholder = stringResource(R.string.birthday_placeholder),
                                onValueChange = { fieldValue ->
                                    if (fieldValue.text.length <= 8) {
                                        birthdayTextFieldValue = fieldValue
                                        onBirthdayChanged(fieldValue.text)
                                    }
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
                            onClick = { onClickSaveButton() },
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
            onNicknameChanged = {},
            onBirthdayChanged = {},
            onFocusChanged = { _, _ -> },
            onRequestExitDialog = {},
            onDisMissExitDialog = {},
            onRequestProfileEditModal = {},
            onDisMissProfileEditModal = {},
            onUpdateProfileImage = {},
            onClickSaveButton = {}
        )
    }
}