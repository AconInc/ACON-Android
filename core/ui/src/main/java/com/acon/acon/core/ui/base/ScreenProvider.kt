package com.acon.acon.core.ui.base

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

interface ScreenProvider {

    @SuppressLint("ComposableNaming")
    @Composable
    fun provide()
}