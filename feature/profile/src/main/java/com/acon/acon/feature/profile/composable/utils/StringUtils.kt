package com.acon.acon.feature.profile.composable.utils

fun Char.isKorean(): Boolean {
    return this in '\uAC00'..'\uD7A3' || // 가 ~ 힣
            this in '\u3131'..'\u318E' || // ㄱ ~ ㅣ
            this in '\u1100'..'\u11FF'    // 초성/중성/종성 자모
}

fun Char.isAllowedChar(): Boolean {
    return this in 'a'..'z' ||
            this in 'A'..'Z' ||
            this in '0'..'9' ||
            this in '가'..'힣' ||
            this in 'ㄱ'..'ㅎ' ||
            this in 'ㅏ'..'ㅣ' ||
            this == '.' || this == '_'
}