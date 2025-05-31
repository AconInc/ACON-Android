package com.acon.acon.feature.profile.composable.utils

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

fun Char.isKorean(): Boolean {
    return this in '\uAC00'..'\uD7A3' || // 가 ~ 힣
            this in '\u3131'..'\u318E' || // ㄱ ~ ㅣ
            this in '\u1100'..'\u11FF'    // 초성/중성/종성 자모
}

fun Char.isAllowedChar(): Boolean {
    return this in 'a'..'z' ||
            this in 'A'..'Z' ||
            this in '0'..'9' ||
            this == '.' ||
            this == '_' ||
            this.isKorean()
}

fun TextFieldValue.limitedNicknameTextFieldValue(
    maxLength: Int = 14,
    isAllowed: (Char) -> Boolean = { it.isAllowedChar() }
): TextFieldValue {
    val filtered = text.filter(isAllowed)
    val limitedText = filtered.take(maxLength)
    val limitedSelection = TextRange(
        limitedText.length.coerceAtMost(selection.start),
        limitedText.length.coerceAtMost(selection.end)
    )
    return copy(
        text = limitedText,
        selection = limitedSelection
    )
}

fun String.limitedNickname(
    maxLength: Int = 14,
    isAllowed: (Char) -> Boolean = { it.isAllowedChar() },
): Pair<String, Int> {
    val limitedNickname = this.filter(isAllowed).take(maxLength)
    return limitedNickname to limitedNickname.length
}