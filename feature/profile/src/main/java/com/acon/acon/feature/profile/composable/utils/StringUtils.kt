package com.acon.acon.feature.profile.composable.utils

fun Char.isAllowedChar(): Boolean {
    return this in 'a'..'z' ||
            this in 'A'..'Z' ||
            this in '0'..'9' ||
            this == '.' ||
            this == '_'
}

fun String.limitedNickname(
    maxLength: Int = 14
): Pair<String, Int> {
    val limitedNickname = this.take(maxLength)
    return limitedNickname to limitedNickname.length
}