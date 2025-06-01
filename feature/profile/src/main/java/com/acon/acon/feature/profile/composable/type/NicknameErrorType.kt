package com.acon.acon.feature.profile.composable.type

import com.acon.acon.core.designsystem.R

sealed class NicknameErrorType {
    data object Invalid : NicknameErrorType()
    data object Duplicate : NicknameErrorType()
}

fun NicknameErrorType.validMessageResId(): Int = when (this) {
    NicknameErrorType.Invalid -> R.string.nickname_error_invalid
    NicknameErrorType.Duplicate -> R.string.nickname_error_duplicate
}

fun NicknameErrorType.contentDescriptionResId(): Int = when (this) {
    NicknameErrorType.Invalid-> R.string.content_description_empty_nickname
    NicknameErrorType.Duplicate -> R.string.content_description_duplicate_nickname
}