package com.acon.feature.profile.screen.profileMod

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.acon.core.designsystem.component.textfield.TextFieldStatus
import com.acon.domain.repository.UploadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ProfileModViewModel @Inject constructor(
    private val profileRepository: UploadRepository //바꿔야 함
) : ViewModel(), ContainerHost<ProfileModState, ProfileModSideEffect> {

    override val container = container<ProfileModState, ProfileModSideEffect>(ProfileModState())

    fun onFocusChanged(isFocused: Boolean) = intent {
        reduce {
            state.copy(
                nickNameFieldStatus = if (isFocused) TextFieldStatus.Focused else TextFieldStatus.Inactive,
                birthdayFieldStatus = if (isFocused) TextFieldStatus.Focused else TextFieldStatus.Inactive
            )
        }
    }

    private fun Char.isAllowedChar(): Boolean {
        return this in 'a'..'z' ||
                this in 'A'..'Z' ||
                this in '0'..'9' ||
                this in '가'..'힣' ||
                this == '.' || this == '_'
    }

    fun onNicknameChanged(text: String) = intent {
        val filteredText = text.filter { it.isAllowedChar() } //허용된 문자만 써지도록 입력 마스크 적용
        reduce {
            state.copy(nickNameState = filteredText, isTyping = true) //작성하는 텍스트 계속 보이기, 작성 중인 경우 잠시 대기
        }

        // 입력이 0.5초간 멈춘 후 유효성 검사 시작
        delay(500L)

        // 특수문자, 제3언어 확인
        val invalidChars = text.any { it in "!@#$%^&*()-=+[]{};:'\",<>/?\\|" }
        val invalidLang = text.any { it !in 'a'..'z' && it !in 'A'..'Z' && it !in '0'..'9' && it !in '가'..'힣' && it !in listOf('.', '_') }

        reduce {
            state.copy(
                hasInvalidChar = invalidChars, // 이 상태에 따라 UI에서 알아서 에러 메시지 띄움
                hasInvalidLang = invalidLang,
                isTyping = false,
            )
        }

        //2초 뒤에 경고 문구 없애기
        if (invalidChars || invalidLang) {
            delay(2000L)
            reduce {
                state.copy(
                    hasInvalidChar = false,
                    hasInvalidLang = false
                )
            }
        }

        // 중복 닉네임 확인하기
        val alreadyUsedName : Boolean = false  // <- 여기서 서버로 요청 보내서 true/false 값 받아오도록 함. 0.5마다 자동으로 요청 보내서 바뀌면 됨.
        reduce {
            state.copy(alreadyUsedName = alreadyUsedName, isTyping = false)  // 이 상태에 따라 UI에서 알아서 에러 메시지 띄움
        }

        //모든 검사를 통과한 경우 (위 2개 조건은 어차피 필터링에서 걸리므로 상관 X)
        if (filteredText.length <= 16 && !alreadyUsedName){
            reduce {
                state.copy(
                    nickNameState = filteredText,
                    nickNameFieldStatus = if (filteredText.isNotEmpty()) TextFieldStatus.Active else TextFieldStatus.Focused,
                    isNicknameValid = true, // alreadyUsedName 까지 통과되면 자동으로 valid해짐 (모든 조건 통과)
                    isTyping = false
                )
            }
        }
    }

    fun onBirthdayChanged(text: String) = intent {
        val formattedText = formatBirthdayInput(text)
        val errors = mutableListOf<String>()
        var isValidDate : Boolean = false

        if (formattedText.length == 10) {
            isValidDate = validateBirthday(formattedText)
            if (!isValidDate) {
                errors.add("정확한 생년월일을 적어주세요")
            }
        }

        reduce {
            state.copy(
                birthdayState = formattedText,
                birthdayFieldStatus = if (formattedText.isNotEmpty() && errors.isEmpty()) TextFieldStatus.Active else TextFieldStatus.Error,
                birthdayErrorMessages = errors,
                isBirthdayValid = isValidDate
            )
        }
    }

    private fun formatBirthdayInput(input: String): String {
        val digitsOnly = input.filter { it.isDigit() }

        return when {
            digitsOnly.length <= 4 -> digitsOnly
            digitsOnly.length <= 6 -> "${digitsOnly.substring(0, 4)}.${digitsOnly.substring(4)}"
            digitsOnly.length <= 8 -> "${digitsOnly.substring(0, 4)}.${digitsOnly.substring(4, 6)}.${digitsOnly.substring(6)}"
            else -> "${digitsOnly.substring(0, 4)}.${digitsOnly.substring(4, 6)}.${digitsOnly.substring(6, 8)}"
        }
    }

    private fun validateBirthday(birthday: String): Boolean {
        val parts = birthday.split(".")
        if (parts.size != 3) return false

        val year = parts[0].toIntOrNull() ?: return false
        val month = parts[1].toIntOrNull() ?: return false
        val day = parts[2].toIntOrNull() ?: return false

        val currentYear = java.time.Year.now().value
        if (year > currentYear) return false

        if (month !in 1..12) return false

        val maxDays = when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (java.time.Year.isLeap(year.toLong())) 29 else 28
            else -> return false
        }
        if (day !in 1..maxDays) return false

        return true
    }

    fun showDialog() = intent {
        reduce {
            state.copy(showDialog = true)
        }
    }

    fun hideDialog() = intent {
        reduce {
            state.copy(showDialog = false)
        }
    }

    fun fetchVerifiedAreaList(){
        //서버 통해서 사용자가 인증 받은 동네 리스트 가져오기
        //가져온 리스트로 verifiedAreaList 관리하기
    }

    fun removeVerifiedArea(area: String) = intent {
        val updatedList = state.verifiedAreaList.filterNot { it == area }

        reduce {
            state.copy(
                verifiedAreaList = updatedList,
                showAreaDeleteDialog = false
            )
        }
    }

    fun showAreaDeleteDialog(area: String) = intent {
        reduce {
            state.copy(
                showAreaDeleteDialog = true,
                selectedArea = area
            )
        }
    }

    fun hideAreaDeleteDialog() = intent {
        reduce {
            state.copy(showAreaDeleteDialog = false)
        }
    }

    fun onProfileClicked() = intent {
        reduce {
            state.copy(requestPhotoPermission = true)
        }
    }

    fun onPermissionHandled() = intent {
        reduce {
            state.copy(requestPhotoPermission = false)
        }
    }

    fun showPermissionDialog() = intent {
        reduce {
            state.copy(showPermissionDialog = true)
        }
    }

    fun hidePermissionDialog() = intent {
        reduce {
            state.copy(showPermissionDialog = false)
        }
    }

    fun onPermissionSettingClick(packageName: String) = intent {
        postSideEffect(ProfileModSideEffect.NavigateToSettings(packageName))
        reduce {
            state.copy(showPermissionDialog = false)
        }
    }

    private fun navigateBack() = intent {
        postSideEffect(ProfileModSideEffect.NavigateBack)
    }
}


data class ProfileModState(

    val isTyping: Boolean = false,

    val nickNameFieldStatus: TextFieldStatus = TextFieldStatus.Inactive,
    val nickNameState: String = "",
    val isNicknameValid: Boolean = false,


    //닉네임 에러 상태 구분
    val hasInvalidChar: Boolean = false, // 특수기호 에러 상태
    val hasInvalidLang: Boolean = false, // 제3언어 에러 상태
    val alreadyUsedName: Boolean = false, //이미 사용 중인 상태

    val birthdayFieldStatus: TextFieldStatus = TextFieldStatus.Inactive,
    val birthdayState: String = "",
    val birthdayErrorMessages: List<String> = emptyList(),
    val isBirthdayValid: Boolean = false,

    val verifiedAreaList: List<String> = listOf("쌍문동"),

    val showDialog: Boolean = false,
    val showAreaDeleteDialog: Boolean = false,
    val selectedArea: String? = null,

    val requestPhotoPermission: Boolean = false,
    val showPermissionDialog: Boolean = false,
    )

sealed interface ProfileModSideEffect {
    data object NavigateBack : ProfileModSideEffect
    data class NavigateToSettings(val packageName: String) : ProfileModSideEffect
    data object NavigateToCustomGallery : ProfileModSideEffect
}