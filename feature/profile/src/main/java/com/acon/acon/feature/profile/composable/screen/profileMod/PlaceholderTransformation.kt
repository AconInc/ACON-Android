package com.acon.acon.feature.profile.composable.screen.profileMod

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PlaceholderTransformation(private val placeholder: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            AnnotatedString(if (text.text.isEmpty()) placeholder else text.text),
            OffsetMapping.Identity
        )
    }
}
