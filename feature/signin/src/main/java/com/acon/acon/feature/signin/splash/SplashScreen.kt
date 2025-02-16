package com.acon.acon.feature.signin.splash

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun SplashScreen(
    state: SplashUiState,
    onAnimationEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(enabled = true) {}

    when(state) {
        SplashUiState.StartSplash -> {
            Column(
                modifier = modifier
                    .background(AconTheme.color.Gray9),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.Asset("acon_splash_lottie.json")
                )
                val logoAnimationState = animateLottieCompositionAsState(composition = composition)
                val textAlpha by animateFloatAsState(
                    targetValue = if (logoAnimationState.value >= .99f) 1f else 0f ,
                    animationSpec = tween(1000),
                    label = stringResource(com.acon.acon.feature.signin.R.string.splash_text_animation_label)
                )

                Column(
                    modifier = Modifier
                        .padding(bottom = 320.dp),
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = { logoAnimationState.progress },
                        modifier = Modifier
                            .width(width = 320.dp)
                    )

                    Text(
                        modifier = Modifier
                            .alpha(textAlpha)
                            .align(Alignment.CenterHorizontally),
                        text = stringResource(com.acon.acon.feature.signin.R.string.splash_text),
                        color = AconTheme.color.White,
                        style = AconTheme.typography.head7_18_sb,
                        textAlign = TextAlign.Center
                    )
                }

                val isTextAnimationEnded = remember { mutableStateOf(false) }

                LaunchedEffect(textAlpha) {
                    if (textAlpha == 1f && !isTextAnimationEnded.value) {
                        isTextAnimationEnded.value = true
                        onAnimationEnd()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    AconTheme {
        SplashScreen(
            state = SplashUiState.StartSplash,
            onAnimationEnd = {},
        )
    }
}