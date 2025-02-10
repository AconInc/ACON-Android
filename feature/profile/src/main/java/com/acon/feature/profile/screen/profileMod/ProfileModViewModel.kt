package com.acon.feature.profile.screen.profileMod

import androidx.lifecycle.ViewModel
import com.acon.core.designsystem.component.textfield.TextFieldStatus
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
        var errIcon : Int = 0

        if (text.isEmpty()){
            errors.add("닉네임을 입력해주세요")
            errIcon = R.drawable.and_ic_error_20
        }

        if (!text.matches("^[a-zA-Z0-9가-힣._]*$".toRegex())) {
            errors.add("._ 이외의 특수기호는 사용할 수 없어요")
            errIcon = R.drawable.and_ic_error_20
        }

        if (text.any { it !in ('a'..'z') && it !in ('A'..'Z') && it !in ('0'..'9') && it !in '가'..'힣' && it !in listOf('.', '_') }) {
            errors.add("한국어, 영어 이외의 언어는 사용할 수 없어요")
            errIcon = R.drawable.and_ic_error_20
        }

        if (text.length > 16){
            errors.add("닉네임은 16자 이하여야 해요")
            errIcon = R.drawable.and_ic_error_20
        }

        if (errors.isEmpty()) {
            errors.add("사용할 수 있는 닉네임이에요")
            errIcon = R.drawable.and_ic_local_check_mark_20
        }

        reduce {
            state.copy(
                nickNameState = text,
                nickNameFieldStatus = if (text.isNotEmpty()) TextFieldStatus.Active else TextFieldStatus.Focused,
                nickNameErrorMessages = errors,
                nickNameErrorIcon = errIcon
            )
        }
    }

    fun onBirthdayChanged(text: String) = intent {
        val formattedText = formatBirthdayInput(text)
        reduce {
            state.copy(
                birthdayState = formattedText,
                birthdayFieldStatus = if (text.isNotEmpty()) TextFieldStatus.Active else TextFieldStatus.Focused
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

    private fun navigateBack() = intent {
        postSideEffect(ProfileModSideEffect.NavigateBack)
    }
}


data class ProfileModState(

    val nickNameFieldStatus: TextFieldStatus = TextFieldStatus.Inactive,
    val nickNameState: String = "",
    val nickNameErrorMessages: List<String> = listOf("닉네임을 입력해주세요"),
    val nickNameErrorIcon: Int = R.drawable.and_ic_error_20,

    val birthdayFieldStatus: TextFieldStatus = TextFieldStatus.Inactive,
    val birthdayState: String = "",
)

sealed interface ProfileModSideEffect {
    data object NavigateToSuccess : ProfileModSideEffect
    data object NavigateBack : ProfileModSideEffect
}