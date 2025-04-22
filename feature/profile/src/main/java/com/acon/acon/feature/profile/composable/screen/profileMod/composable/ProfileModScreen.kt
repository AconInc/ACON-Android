package com.acon.acon.feature.profile.composable.screen.profileMod.composable

import android.content.Intent
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.acon.acon.feature.profile.composable.component.TextFieldStatus
import com.acon.acon.feature.profile.composable.component.addFocusCleaner
import com.acon.acon.feature.profile.composable.screen.profileMod.BirthdayStatus
import com.acon.acon.feature.profile.composable.screen.profileMod.NicknameStatus
import com.acon.acon.feature.profile.composable.screen.profileMod.ProfileModSideEffect
import com.acon.acon.feature.profile.composable.screen.profileMod.ProfileModState
import com.acon.acon.feature.profile.composable.screen.profileMod.ProfileModViewModel
import com.acon.acon.feature.profile.composable.type.FocusType
import com.acon.acon.feature.profile.composable.type.ProfileUpdateResult
import com.acon.acon.feature.profile.composable.utils.BirthdayTransformation
import com.acon.acon.feature.profile.composable.utils.isAllowedChar
import com.acon.acon.feature.profile.composable.utils.isKorean
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ProfileModScreenContainer(
    modifier: Modifier = Modifier,
    viewModel: ProfileModViewModel = hiltViewModel(),
    selectedPhotoId: String = "",
    backToProfile: () -> Unit = {},
    onNavigateToProfile: (ProfileUpdateResult) -> Unit = { },
    onNavigateToCustomGallery: () -> Unit = {},
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(selectedPhotoId) {
        if (selectedPhotoId.isNotEmpty()) {
            viewModel.updateProfileImage(selectedPhotoId)
        } else {
            viewModel.updateProfileImage(state.originalPhotoUri)
        }
    }


    viewModel.collectSideEffect { effect ->
        when (effect) {
            is ProfileModSideEffect.NavigateToSettings -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", effect.packageName, null)
                }
                context.startActivity(intent)
            }

            is ProfileModSideEffect.NavigateBack -> {
                backToProfile()
            }

            is ProfileModSideEffect.NavigateToCustomGallery -> {
                onNavigateToCustomGallery()
            }

            is ProfileModSideEffect.UpdateProfileImage -> {
                selectedPhotoId.let {
                    viewModel.updateProfileImage(selectedPhotoId)
                }
            }

            is ProfileModSideEffect.NavigateToProfileSuccess -> {
                onNavigateToProfile(ProfileUpdateResult.SUCCESS)
            }

            is ProfileModSideEffect.NavigateToProfileFailed -> {
                onNavigateToProfile(ProfileUpdateResult.FAILURE)
            }
        }
    }

    if (state.requestPhotoPermission) {
        CheckAndRequestPhotoPermission(
            onPermissionGranted = {
                viewModel.onPermissionHandled()
                onNavigateToCustomGallery()
            },
            onPermissionDenied = {
                viewModel.showPermissionDialog()
                viewModel.onPermissionHandled()
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
                viewModel.hideExitDialog()
            },
            onClickLeft = { // 나가기 (프로필 뷰로 이동)
                backToProfile()
            },
            onClickRight = { // 계속 작성하기
                viewModel.hideExitDialog()
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
            onDismissRequest = { viewModel.hidePermissionDialog() },
            onClickLeft = { viewModel.hidePermissionDialog() },
            onClickRight = { viewModel.onPermissionSettingClick(context.packageName) },
            isImageEnabled = false
        )
    }

    if (state.showPhotoEditDialog) {
        CustomModalBottomSheet(
            onDismiss = viewModel::hideProfileEditDialog,
            onGallerySelect = viewModel::onProfileClicked,
            onDefaultImageSelect = { viewModel.updateProfileImage("") },
        )
    }

    ProfileModScreen(
        modifier = modifier,
        state = state,
        onNicknameChanged = viewModel::onNicknameChanged,
        onBirthdayChanged = viewModel::onBirthdayChanged,
        onFocusChanged = viewModel::onFocusChanged,
        onBackClicked = viewModel::showExitDialog,
        onSaveClicked = viewModel::getPreSignedUrl,
        onProfileClicked = viewModel::showProfileEditDialog,
    )
}

@Composable
fun ProfileModScreen(
    modifier: Modifier = Modifier,
    state: ProfileModState,
    onNicknameChanged: (String) -> Unit = {},
    onBirthdayChanged: (String) -> Unit = {},
    onFocusChanged: (Boolean, FocusType) -> Unit = { _, _ -> },
    onBackClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onProfileClicked: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val nickNameFocusRequester = remember { FocusRequester() }
    val birthDayFocusRequester = remember { FocusRequester() }

    // 커서 위치를 관리하기 위해 사용
    var nicknameText by remember { mutableStateOf(TextFieldValue("")) }
    var birthdayText by remember { mutableStateOf(TextFieldValue("")) }

    // 서버로 부터 받아온 초기값 설정 (닉네임)
    LaunchedEffect(state.nickNameState) {
        if (nicknameText.text != state.nickNameState) {
            nicknameText = TextFieldValue(state.nickNameState)
        }
    }

    // 서버로 부터 받아온 초기값 설정 (생일)
    LaunchedEffect(state.birthdayState) {
        if (birthdayText.text != state.birthdayState) {
            birthdayText = TextFieldValue(state.birthdayState)
        }
    }

    BackHandler(enabled = true) {
        onBackClicked()
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
                IconButton(onClick = onBackClicked) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = com.acon.acon.core.designsystem.R.drawable.ic_arrow_left_28),
                        contentDescription = "Back",
                    )
                }
            },
            content = {
                Text(
                    text = "프로필 편집",
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
                            photoUri = state.selectedPhotoUri
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.and_ic_profile_img_edit),
                            contentDescription = "Profile edit icon",
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
                            text = "닉네임",
                            style = AconTheme.typography.head8_16_sb,
                            color = AconTheme.color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "*",
                            style = AconTheme.typography.head8_16_sb,
                            color = AconTheme.color.Main_org1
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    ProfileTextField(
                        status = state.nickNameFieldStatus,
                        focusType = FocusType.Nickname,
                        focusRequester = nickNameFocusRequester,
                        value = nicknameText,
                        placeholder = "16자 이내 영문, 한글, 숫자, ., _만 사용 가능",
                        isTyping = (state.nicknameStatus == NicknameStatus.Typing),
                        onTextChanged = { fieldValue ->
                            val inputText = fieldValue.text
                            var count = 0
                            var textLimit = false

                            val displayText = buildString {
                                for (char in inputText) {
                                    val weight = if (char.isKorean()) 2 else 1
                                    if (count + weight > 16) {
                                        textLimit = true
                                        break
                                    }
                                    if (char.isAllowedChar()) {
                                        append(char)
                                        count += weight
                                    }
                                }
                            }
                            if (!textLimit || displayText.length <= nicknameText.text.length) {
                                nicknameText = fieldValue.copy(text = displayText)
                                onNicknameChanged(inputText)
                            }
                        },
                        onFocusChanged = onFocusChanged,
                        onClick = {
                            nickNameFocusRequester.requestFocus()
                        },
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
                            when (val status = state.nicknameStatus) {
                                is NicknameStatus.Empty -> {
                                    NicknameErrMessageRow(
                                        modifier = modifier,
                                        iconRes = ImageVector.vectorResource(R.drawable.and_ic_error_20),
                                        errMessage = "닉네임을 입력해 주세요",
                                        textColor = AconTheme.color.Error_red1
                                    )
                                }

                                is NicknameStatus.Error -> {
                                    status.errorTypes.forEach { error ->
                                        NicknameErrMessageRow(
                                            modifier = modifier,
                                            iconRes = ImageVector.vectorResource(R.drawable.and_ic_error_20),
                                            errMessage = error.message,
                                            textColor = AconTheme.color.Error_red1
                                        )
                                    }
                                }

                                is NicknameStatus.Valid -> {
                                    NicknameErrMessageRow(
                                        modifier = modifier,
                                        iconRes = ImageVector.vectorResource(R.drawable.and_ic_local_check_mark_20),
                                        errMessage = "사용할 수 있는 닉네임이에요",
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
                                text = "/16",
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
                            text = "생년월일",
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
                        value = birthdayText,
                        placeholder = "YYYY.MM.DD",
                        onTextChanged = { fieldValue ->
                            val digitsOnly = fieldValue.text.filter { it.isDigit() }.take(8)
                            val cursorPos = minOf(digitsOnly.length, fieldValue.selection.start)

                            birthdayText = fieldValue.copy(
                                text = digitsOnly,
                                selection = TextRange(cursorPos)
                            )
                            onBirthdayChanged(digitsOnly)
                        },
                        onFocusChanged = onFocusChanged,
                        visualTransformation = BirthdayTransformation(),
                        onClick = {
                            birthDayFocusRequester.requestFocus()
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    when (val status = state.birthdayStatus) {
                        is BirthdayStatus.Valid -> {
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        is BirthdayStatus.Invalid -> {
                            NicknameErrMessageRow(
                                modifier = modifier,
                                iconRes = ImageVector.vectorResource(R.drawable.and_ic_error_20),
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
                text = "저장",
                textStyle = AconTheme.typography.head8_16_sb,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                disabledBackgroundColor = AconTheme.color.Gray7,
                enabledBackgroundColor = AconTheme.color.Gray5,
                disabledTextColor = AconTheme.color.Gray5,
                enabledTextColor = AconTheme.color.White,
                onClick = onSaveClicked,
                isEnabled = (state.nicknameStatus == NicknameStatus.Valid) &&
                        (
                                (state.nickNameState != state.originalNickname) ||
                                        (state.birthdayState != state.originalBirthday && state.birthdayStatus == BirthdayStatus.Valid) ||
                                        (state.selectedPhotoUri != state.originalPhotoUri)
                                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileModScreenPreview() {
    val nickNameState = remember { mutableStateOf("") }
    val birthdayState = remember { mutableStateOf("") }

    ProfileModScreen(
        onFocusChanged = { _, _ -> },
        state = ProfileModState(
            nickNameState = nickNameState.value,
            birthdayState = birthdayState.value,
            nickNameFieldStatus = TextFieldStatus.Inactive,
            birthdayFieldStatus = TextFieldStatus.Inactive
        ),
        onNicknameChanged = { newValue -> nickNameState.value = newValue },
        onBirthdayChanged = { newValue -> birthdayState.value = newValue },
        onBackClicked = {},
        onSaveClicked = {},
    )
}


