package com.acon.feature.profile.screen.profileMod.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.core.designsystem.component.button.AconFilledLargeButton
import com.acon.core.designsystem.component.textfield.AconTextField
import com.acon.core.designsystem.component.textfield.TextFieldStatus
import com.acon.core.designsystem.component.textfield.addFocusCleaner
import com.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.core.designsystem.theme.AconTheme
import com.acon.feature.profile.R
import com.acon.feature.profile.component.VerifiedAreaChip
import com.acon.feature.profile.screen.profileMod.ProfileModState
import com.acon.feature.profile.screen.profileMod.ProfileModViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ProfileModScreenContainer(
    modifier: Modifier = Modifier,
    viewModel: ProfileModViewModel = hiltViewModel(),
    onNavigateToProfile: () -> Unit = {},
    onNavigateToAreaVerification: () -> Unit = {},
    onBackClicked: () -> Unit = {}
) {
    val state = viewModel.collectAsState().value

    ProfileModScreen(
        modifier = modifier,
        state = state,
        onNicknameChanged = viewModel::onNicknameChanged,
        onBirthdayChanged = viewModel::onBirthdayChanged,
        onFocusChanged = viewModel::onFocusChanged,
        onBackClicked = onBackClicked,
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToAreaVerification = onNavigateToAreaVerification,
        onRemoveArea = viewModel::removeVerifiedArea,
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
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = AconTheme.color.Gray9)
            .statusBarsPadding()
            .navigationBarsPadding()
            .addFocusCleaner(focusManager)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 130.dp)
        ){
            Image(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                painter = painterResource(id = R.drawable.img_profile_basic_80),
                contentDescription = "profile",
                contentScale = ContentScale.Crop,
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            //닉네임 입력 필드
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
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
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ){
                    // 에러 메시지 영역
                    Column( // 에러 메시지를 세로로 나열
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        state.nickNameErrorMessages.forEach { errorMessage ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (state.isNicknameValid) ImageVector.vectorResource(R.drawable.and_ic_local_check_mark_20)
                                                    else ImageVector.vectorResource(R.drawable.and_ic_error_20),
                                    contentDescription = "Error Icon",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.padding(horizontal = 2.dp).size(16.dp)
                                )
                                Text(
                                    text = errorMessage,
                                    style = AconTheme.typography.subtitle2_14_med,
                                    color = if (state.isNicknameValid) AconTheme.color.Success_blue1 else AconTheme.color.Error_red1
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
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
                modifier = Modifier.padding(horizontal = 16.dp)
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
                if (state.birthdayErrorMessages.isNotEmpty()){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (state.isNicknameValid) ImageVector.vectorResource(R.drawable.and_ic_local_check_mark_20)
                            else ImageVector.vectorResource(R.drawable.and_ic_error_20),
                            contentDescription = "Error Icon",
                            tint = AconTheme.color.Error_red1,
                            modifier = Modifier.padding(horizontal = 2.dp).size(16.dp)
                        )
                        Text(
                            text = state.birthdayErrorMessages.first(),
                            style = AconTheme.typography.subtitle2_14_med,
                            color = AconTheme.color.Error_red1
                        )
                    }
                }
            }

            //동네인증 필드
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
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

            Spacer(modifier = Modifier.height(150.dp))

            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                AconFilledLargeButton(
                    text = "저장",
                    textStyle = AconTheme.typography.head8_16_sb,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 36.dp),
                    disabledBackgroundColor = AconTheme.color.Gray7,
                    enabledBackgroundColor = AconTheme.color.Gray5,
                    disabledTextColor = AconTheme.color.Gray5,
                    enabledTextColor = AconTheme.color.White,
                    onClick = onNavigateToProfile,
                    isEnabled = state.isNicknameValid && state.isBirthdayValid && state.verifiedAreaList.isNotEmpty()
                )
            }
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
