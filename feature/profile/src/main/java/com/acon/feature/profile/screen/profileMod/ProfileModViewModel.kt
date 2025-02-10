package com.acon.feature.profile.screen.profileMod

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.acon.core.designsystem.component.textfield.TextFieldStatus
import com.acon.core.designsystem.theme.AconTheme
import com.acon.domain.repository.UploadRepository
import com.acon.feature.profile.R
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ProfileModViewModel @Inject constructor(
    private val profileRepository: UploadRepository
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

    fun onNicknameChanged(text: String) = intent {
        val errors = mutableListOf<String>()
        val isValid = validateNickname(text, errors)

        if (text.length <= 16){
            reduce {
                state.copy(
                    nickNameState = text,
                    nickNameFieldStatus = if (text.isNotEmpty()) TextFieldStatus.Active else TextFieldStatus.Focused,
                    nickNameErrorMessages = errors,
                    isNicknameValid = isValid
                )
            }
        }
    }

    private fun validateNickname(text: String, errors:MutableList<String>): Boolean {
        var isValid = true

        if (text.isEmpty()){
            errors.add("닉네임을 입력해주세요")
            isValid = false
        }

        if (!text.matches("^[a-zA-Z0-9가-힣._]*$".toRegex())) {
            errors.add("._ 이외의 특수기호는 사용할 수 없어요")
            isValid = false
        }

        if (text.any { it !in ('a'..'z') && it !in ('A'..'Z') && it !in ('0'..'9') && it !in '가'..'힣' && it !in listOf('.', '_') }) {
            errors.add("한국어, 영어 이외의 언어는 사용할 수 없어요")
            isValid = false
        }

        if (errors.isEmpty()) {
            errors.add("사용할 수 있는 닉네임이에요")
        }

        return isValid
    }

    fun onBirthdayChanged(text: String) = intent {
        val formattedText = formatBirthdayInput(text)
        val errors = mutableListOf<String>()

        if (formattedText.length == 10) {
            val isValidDate = validateBirthday(formattedText)
            if (!isValidDate) {
                errors.add("정확한 생년월일을 적어주세요")
            }
        }

        reduce {
            state.copy(
                birthdayState = formattedText,
                birthdayFieldStatus = if (formattedText.isNotEmpty() && errors.isEmpty()) TextFieldStatus.Active else TextFieldStatus.Error,
                birthdayErrorMessages = errors
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

    private fun navigateBack() = intent {
        postSideEffect(ProfileModSideEffect.NavigateBack)
    }
}


data class ProfileModState(

    val nickNameFieldStatus: TextFieldStatus = TextFieldStatus.Inactive,
    val nickNameState: String = "",
    val nickNameErrorMessages: List<String> = listOf("닉네임을 입력해주세요"),
    val isNicknameValid: Boolean = false,

    val birthdayFieldStatus: TextFieldStatus = TextFieldStatus.Inactive,
    val birthdayState: String = "",
    val birthdayErrorMessages: List<String> = emptyList()
)

sealed interface ProfileModSideEffect {
    data object NavigateToSuccess : ProfileModSideEffect
    data object NavigateBack : ProfileModSideEffect
}