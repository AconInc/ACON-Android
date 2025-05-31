package com.acon.acon.feature.profile.composable.type

import com.acon.acon.core.designsystem.R

sealed class NicknameErrorType {
    data object InvalidChar : NicknameErrorType()
    data object InvalidLang : NicknameErrorType()
    data object AlreadyUsed : NicknameErrorType()
}

fun NicknameErrorType.validMessageResId(): Int = when (this) {
    NicknameErrorType.InvalidChar -> R.string.nickname_error_invalid
    NicknameErrorType.InvalidLang -> R.string.nickname_error_invalid_language
    NicknameErrorType.AlreadyUsed -> R.string.nickname_error_duplicate
}

fun NicknameErrorType.contentDescriptionResId(): Int = when (this) {
    NicknameErrorType.InvalidChar-> R.string.content_description_empty_nickname
    NicknameErrorType.InvalidLang -> R.string.content_description_invalid_language
    NicknameErrorType.AlreadyUsed -> R.string.content_description_duplicate_nickname
}