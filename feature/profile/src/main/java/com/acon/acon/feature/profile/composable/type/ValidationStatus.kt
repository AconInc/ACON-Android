package com.acon.acon.feature.profile.composable.type

sealed interface TextFieldStatus {
    data object Empty : TextFieldStatus
    data object Inactive : TextFieldStatus
    data object Focused : TextFieldStatus
    data object Active : TextFieldStatus
    data object Error : TextFieldStatus
    data object Disabled : TextFieldStatus
}

sealed class NicknameErrorType(val message: String) {
    data object InvalidChar : NicknameErrorType("._ 이외의 특수기호는 사용할 수 없어요")
    data object InvalidLang : NicknameErrorType("한국어, 영어 이외의 언어는 사용할 수 없어요")
    data object AlreadyUsed : NicknameErrorType("이미 사용 중인 닉네임이에요.")
}

sealed class NicknameValidationStatus {
    data object Empty : NicknameValidationStatus()
    data object Typing : NicknameValidationStatus()
    data object Valid : NicknameValidationStatus()
    data class Error(val errorTypes: List<NicknameErrorType>) : NicknameValidationStatus()
}

sealed class BirthdayValidationStatus {
    data object Empty : BirthdayValidationStatus()
    data class Invalid(val errorMsg: String?) : BirthdayValidationStatus()
    data object Valid : BirthdayValidationStatus()
}