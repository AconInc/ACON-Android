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
            this.isKorean() ||
            this == '.' || this == '_'
}

fun TextFieldValue.limitedNicknameTextFieldValue(
    maxLength: Int = 14,
    charWeight: (Char) -> Int = { if (it.isKorean()) 2 else 1 },
    isAllowed: (Char) -> Boolean = { it.isAllowedChar() }
): TextFieldValue {
    val filtered = text.filter(isAllowed)
    var count = 0
    var cutIndex = 0
    for (char in filtered) {
        val weight = charWeight(char)
        if (count + weight > maxLength) break
        count += weight
        cutIndex++
    }
    val limitedText = filtered.take(cutIndex)
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
    charWeight: (Char) -> Int = { if (it.isKorean()) 2 else 1 }
): Pair<String, Int> {
    var count = 0
    var cutIndex = 0
    for (char in this) {
        if (!isAllowed(char)) continue
        val weight = charWeight(char)
        if (count + weight > maxLength) break
        count += weight
        cutIndex++
    }
    val limitedNickname = this.filter(isAllowed).take(cutIndex)

    return limitedNickname to count
}