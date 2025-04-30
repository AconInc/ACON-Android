package com.acon.acon.core.designsystem.component.loading

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun AconCircularProgressBar(
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    @RawRes lottie: Int = R.raw.lottie_progress_w,
) {
    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(lottie)
    )

    LottieAnimation(
        modifier = modifier.size(size),
        composition = lottieComposition,
        iterations = Int.MAX_VALUE,
    )
}