package com.acon.acon.core.designsystem.component.radiobutton

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.acon.acon.core.designsystem.theme.AconTheme

data class AconRadioButtonColors(
    val borderColor: Color,
    val selectedContainerColor: Color,
    val unselectedContainerColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color
) {
    companion object {
        @Composable
        fun DeleteAccountRadioButtonColors(): AconRadioButtonColors = AconRadioButtonColors(
            borderColor = AconTheme.color.GlassWhiteDefault,
            selectedContainerColor = AconTheme.color.GlassWhiteDefault,
            unselectedContainerColor = AconTheme.color.Gray900,
            selectedContentColor = AconTheme.color.Gray50,
            unselectedContentColor = AconTheme.color.Gray900
        )
    }
}