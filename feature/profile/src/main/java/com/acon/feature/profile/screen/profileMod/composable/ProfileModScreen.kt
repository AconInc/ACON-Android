package com.acon.feature.profile.screen.profileMod.composable

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.SurfaceCoroutineScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.acon.core.designsystem.component.button.AconFilledLargeButton
import com.acon.core.designsystem.component.dialog.AconTwoButtonDialog
import com.acon.core.designsystem.component.textfield.AconTextField
import com.acon.core.designsystem.component.textfield.TextFieldStatus
import com.acon.core.designsystem.component.textfield.addFocusCleaner
import com.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.core.designsystem.theme.AconTheme
import com.acon.core.utils.feature.permission.CheckAndRequestPhotoPermission
import com.acon.feature.profile.R
import com.acon.feature.profile.component.NicknameErrMessageRow
import com.acon.feature.profile.component.ProfilePhotoBox
import com.acon.feature.profile.component.VerifiedAreaChip
import com.acon.feature.profile.screen.profileMod.BirthdayStatus
import com.acon.feature.profile.screen.profileMod.NicknameStatus
import com.acon.feature.profile.screen.profileMod.ProfileModSideEffect
import com.acon.feature.profile.screen.profileMod.ProfileModState
import com.acon.feature.profile.screen.profileMod.ProfileModViewModel
import kotlinx.coroutines.CoroutineScope
import org.orbitmvi.orbit.compose.collectAsState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ProfileModScreenContainer(
    modifier: Modifier = Modifier,
    viewModel: ProfileModViewModel = hiltViewModel(),
    selectedPhotoId: String = "",
    onNavigateToProfile: () -> Unit = {},
    onNavigateToAreaVerification: () -> Unit = {},
    onNavigateToCustomGallery: () -> Unit = {},
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(selectedPhotoId){ //갤러리 접근해서 사진 String 갖고 돌아오는 경우, ViewModel의 State 값에 uri로 parse해서 저장해줌
        selectedPhotoId.let {
            viewModel.updateProfileImage(selectedPhotoId)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { effect ->
            when (effect) {
                is ProfileModSideEffect.NavigateToSettings -> {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", effect.packageName, null)
                    }
                    context.startActivity(intent)
                }
                is ProfileModSideEffect.NavigateBack -> {
                    onNavigateToProfile() // 변경사항 있으면 저장 안할거냐고 다이얼로그 띄운 다음에 보내야 하나?
                }
                is ProfileModSideEffect.NavigateToCustomGallery -> {
                    onNavigateToCustomGallery()
                }
                is ProfileModSideEffect.UpdateProfileImage -> {
                    selectedPhotoId.let {
                        viewModel.updateProfileImage(selectedPhotoId)
                    }
                }
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

    if (state.showDialog) {
        AconTwoButtonDialog(
            title = stringResource(R.string.profile_mod_alert_title),
            content = stringResource(R.string.profile_mod_alert_description),
            leftButtonContent = stringResource(R.string.profile_mod_alert_left_btn),
            rightButtonContent = stringResource(R.string.profile_mod_alert_right_btn),
            contentImage = 0,
            onDismissRequest = {
                viewModel.hideDialog()
            },
            onClickLeft = { // 나가기 (프로필 뷰로 이동)
                onNavigateToProfile()
            },
            onClickRight = { // 계속 작성하기
                viewModel.hideDialog()
            },
            isImageEnabled = false
        )
    }

    if (state.showAreaDeleteDialog) {
        AconTwoButtonDialog(
            title = stringResource(R.string.delete_area_alert_title, state.selectedArea ?: "이 동네"),
            content = "",
            leftButtonContent = stringResource(R.string.delete_area_alert_left_btn),
            rightButtonContent = stringResource(R.string.delete_area_alert_right_btn),
            contentImage = 0,
            onDismissRequest = {
                viewModel.hideAreaDeleteDialog()
            },
            onClickLeft = { // 취소
                viewModel.hideAreaDeleteDialog()
            },
            onClickRight = { // 선택한 동네 삭제하기
                state.selectedArea?.let { viewModel.removeVerifiedArea(it) }
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

    ProfileModScreen(
        modifier = modifier,
        state = state,
        onNicknameChanged = viewModel::onNicknameChanged,
        onBirthdayChanged = viewModel::onBirthdayChanged,
        onFocusChanged = viewModel::onFocusChanged,
        onBackClicked = viewModel::showDialog,
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToAreaVerification = onNavigateToAreaVerification,
        onRemoveArea = viewModel::showAreaDeleteDialog,
        onProfileClicked = viewModel::onProfileClicked,
    )
}

@Composable
fun ProfileModScreen(
    modifier: Modifier = Modifier,
    state: ProfileModState,
    onNicknameChanged: (String) -> Unit = {},
    onBirthdayChanged: (String) -> Unit = {},
    onFocusChanged: (Boolean) -> Unit = {},
    onBackClicked: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAreaVerification: () -> Unit,
    onRemoveArea: (String) -> Unit = {},
    onProfileClicked: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    //뒤로가기 눌렀을 때 다이얼로그 뜨게 처리
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
                        imageVector = ImageVector.vectorResource(id = com.acon.core.designsystem.R.drawable.ic_arrow_left_28),
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
        ){
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier.weight(1f).aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ){
                        ProfilePhotoBox(
                            modifier = Modifier.fillMaxSize().align(Alignment.Center),
                            photoUri = state.selectedPhotoUri
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.and_ic_profile_img_edit),
                            contentDescription = "Profile edit icon",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .align(alignment = Alignment.BottomEnd)
                                .clickable{ onProfileClicked() }
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                //닉네임 입력 필드
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ){
                        Text(text = "닉네임", style = AconTheme.typography.head8_16_sb, color = AconTheme.color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "*", style = AconTheme.typography.head8_16_sb, color = AconTheme.color.Main_org1)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    AconTextField(
                        status = state.nickNameFieldStatus,
                        text = state.nickNameState,
                        onTextChanged = onNicknameChanged,
                        onFocusChanged = onFocusChanged,
                        placeholder = "16자 이내 영문, 한글, 숫자, ., _만 사용 가능",
                        isTyping = (state.nicknameStatus == NicknameStatus.Typing)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ){
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
                                else -> Unit //Typing 상태일 때는 표시 X
                            }
                        }

                        //글자 수 카운드
                        Row(
                            horizontalArrangement = Arrangement.End
                        ){
                            Text(text = "${state.nickNameState.length}", style = AconTheme.typography.subtitle2_14_med, color = AconTheme.color.White)
                            Text(text = "/16", style = AconTheme.typography.subtitle2_14_med, color = AconTheme.color.Gray5)
                        }
                    }
                }

                //생년월일 필드
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ){
                        Text(text = "생년월일", style = AconTheme.typography.head8_16_sb, color = AconTheme.color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    AconTextField(
                        status = state.birthdayFieldStatus,
                        text = state.birthdayState,
                        onTextChanged = onBirthdayChanged,
                        onFocusChanged = onFocusChanged,
                        placeholder = "ex) 2025.01.01",
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

                //동네인증 필드
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ){
                        Text(text = "인증 동네", style = AconTheme.typography.head8_16_sb, color = AconTheme.color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "*", style = AconTheme.typography.head8_16_sb, color = AconTheme.color.Main_org1)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    VerifiedAreaChip(
                        modifier = Modifier,
                        areaList = state.verifiedAreaList,
                        onAddArea = { onNavigateToAreaVerification() }, //동네인증 페이지로 이동
                        onRemoveArea = onRemoveArea , //areaList에서 현재 동네를 삭제
                        errorMessage = "로컬도토리를 위해 최소 1개의 동네를 인증해주세요"
                    )
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
                onClick = onNavigateToProfile,
                isEnabled = (state.nicknameStatus == NicknameStatus.Valid) &&
                        (state.birthdayStatus == BirthdayStatus.Valid) &&
                        state.verifiedAreaList.isNotEmpty()
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
        state = ProfileModState(
            nickNameState = nickNameState.value,
            birthdayState = birthdayState.value,
            nickNameFieldStatus = TextFieldStatus.Inactive,
            birthdayFieldStatus = TextFieldStatus.Inactive
        ),
        onNicknameChanged = { newValue -> nickNameState.value = newValue },
        onBirthdayChanged = { newValue -> birthdayState.value = newValue },
        onFocusChanged = {},
        onBackClicked = {},
        onNavigateToProfile = {},
        onNavigateToAreaVerification = {},
        onRemoveArea = {}
    )
}
