package com.acon.acon.feature.profile.composable.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class BirthdayTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return birthdayFilter(text)
    }
}

fun birthdayFilter(text: AnnotatedString): TransformedText {
    val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
    val builder = StringBuilder()

    for (i in trimmed.indices) {
        builder.append(trimmed[i])
        if ((i == 3 || i == 5) && i < trimmed.length - 1) {
            builder.append(".")
        }
    }

    val out = builder.toString()

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val clamped = offset.coerceIn(0, trimmed.length)
            return when {
                clamped <= 3 -> clamped
                clamped <= 5 -> (clamped + 1).coerceAtMost(out.length)
                clamped <= 7 -> (clamped + 2).coerceAtMost(out.length)
                else -> out.length
            }
        }

        override fun transformedToOriginal(offset: Int): Int {
            val clamped = offset.coerceIn(0, out.length)
            return when {
                clamped <= 4 -> clamped
                clamped <= 7 -> clamped - 1
                clamped <= 10 -> clamped - 2
                else -> trimmed.length
            }
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}