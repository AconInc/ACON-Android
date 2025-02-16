package com.acon.acon.core.designsystem.keyboard

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity

@Composable
fun keyboardAsState(): State<Int> {
    val ime = WindowInsets.ime
    val density = LocalDensity.current
    return remember(ime, density) {
        derivedStateOf { ime.getBottom(density) }
    }
}