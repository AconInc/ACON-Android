package com.acon.acon.feature.profile.composable.type

sealed interface TextFieldStatus {
    data object Empty : TextFieldStatus
    data object Focused : TextFieldStatus
    data object Inactive : TextFieldStatus
    data object Error : TextFieldStatus
}

sealed class NicknameValidationStatus {
    data object Empty : NicknameValidationStatus()
    data object Typing : NicknameValidationStatus()
    data object Valid : NicknameValidationStatus()
    data class Error(val errorTypes: NicknameErrorType) : NicknameValidationStatus()
}

sealed class BirthdayValidationStatus {
    data object Empty : BirthdayValidationStatus()
    data object Invalid : BirthdayValidationStatus()
    data object Valid : BirthdayValidationStatus()
}