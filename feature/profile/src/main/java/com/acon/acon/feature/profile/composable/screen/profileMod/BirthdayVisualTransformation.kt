package com.acon.acon.feature.profile.composable.screen.profileMod

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class BirthdayVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.filter { it.isDigit() } // 숫자만 허용
        val formatted = StringBuilder()

        for (i in trimmed.indices) {
            if (i == 4 || i == 6) formatted.append(".") // 4번째, 6번째 위치에서 . 추가
            formatted.append(trimmed[i])
        }

        return TransformedText(AnnotatedString(formatted.toString()), birthdayOffsetMapping)
    }

    private val birthdayOffsetMapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return when {
                offset <= 4 -> offset
                offset <= 6 -> offset + 1 // YYYY. 추가 후 조정
                else -> offset + 2 // YYYY.MM. 추가 후 조정
            }
        }

        override fun transformedToOriginal(offset: Int): Int {
            return when {
                offset <= 4 -> offset
                offset <= 7 -> offset - 1 // YYYY. 제거 후 조정
                else -> offset - 2 // YYYY.MM. 제거 후 조정
            }
        }
    }
}
