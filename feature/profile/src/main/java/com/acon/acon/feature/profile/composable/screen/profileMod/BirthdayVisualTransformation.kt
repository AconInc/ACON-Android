package com.acon.acon.feature.profile.composable.screen.profileMod

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class BirthdayVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.filter { it.isDigit() }
        val formatted = StringBuilder()

        for (i in trimmed.indices) {
            if (i == 4 || i == 6) formatted.append(".")
            formatted.append(trimmed[i])
        }

        return TransformedText(AnnotatedString(formatted.toString()), birthdayOffsetMapping)
    }

    private val birthdayOffsetMapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return when {
                offset <= 4 -> offset
                offset <= 6 -> offset + 1
                else -> offset + 2
            }
        }

        override fun transformedToOriginal(offset: Int): Int {
            return when {
                offset <= 4 -> offset
                offset <= 7 -> offset - 1
                else -> offset - 2
            }
        }
    }
}
