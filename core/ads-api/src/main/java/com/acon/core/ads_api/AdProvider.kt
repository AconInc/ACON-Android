package com.acon.core.ads_api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier

interface AdProvider {

    @Composable
    fun NativeAd(modifier: Modifier)
}

val LocalSpotListAdProvider = staticCompositionLocalOf<AdProvider> {
    error("AdProvider가 제공되지 않았습니다.")
}