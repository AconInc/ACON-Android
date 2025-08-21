package com.acon.feature.onboarding.introduce.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.animation.slidingFadeIn
import com.acon.acon.core.designsystem.component.tag.AconTag
import com.acon.acon.core.designsystem.effect.effect.shadowLayerBackground
import com.acon.acon.core.designsystem.effect.screenDefault
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.ui.base.ScreenProvider
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

internal class IntroduceLocalReviewScreenProvider : ScreenProvider {

    @Composable
    override fun provide() {
        IntroduceLocalReviewScreen(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(top = 54.dp)
        )
    }
}

private const val LOCAL_TAG_ALPHA_ANIMATION_DURATION_MS = 1000
private const val MESSAGE_APPEAR_ANIMATION_DELAY_MS = 200
private const val MESSAGE_APPEAR_ANIMATION_DURATION_MS = 500

@Composable
internal fun IntroduceLocalReviewScreen(
    modifier: Modifier = Modifier
) {

    var playAcornLottieAnimation by remember {
        mutableStateOf(false)
    }
    val acornLottieComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.acorn5)
    )
    val acornLottieProgress = animateLottieCompositionAsState(
        composition = acornLottieComposition,
        isPlaying = playAcornLottieAnimation,
        iterations = 1,
        speed = 1f
    )

    var isAcornLottieAnimationEnded by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        delay((MESSAGE_APPEAR_ANIMATION_DELAY_MS + MESSAGE_APPEAR_ANIMATION_DURATION_MS).toLong())
        playAcornLottieAnimation = true

        delay(100)
        snapshotFlow { acornLottieProgress.isAtEnd }.collect { isEnded ->
            if (isEnded)
                isAcornLottieAnimationEnded = true
        }
    }

    val localTagAlphaAnimation by rememberInfiniteTransition(label = "tagAlphaInfinite").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = LOCAL_TAG_ALPHA_ANIMATION_DURATION_MS, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tagAlphaAnimation"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .slidingFadeIn(
                    durationMillis = MESSAGE_APPEAR_ANIMATION_DURATION_MS,
                    delayMillis = MESSAGE_APPEAR_ANIMATION_DELAY_MS
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.introduce_local_review_title),
                color = AconTheme.color.White,
                style = AconTheme.typography.Title2,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = stringResource(R.string.introduce_local_review_content),
                color = AconTheme.color.White,
                style = AconTheme.typography.Title4,
                modifier = Modifier.padding(top = 24.dp),
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            if (LocalInspectionMode.current.not())
                LottieAnimation(
                    composition = acornLottieComposition,
                    progress = { acornLottieProgress.progress },
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f)
                )
            else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AconTheme.color.Gray700),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "로띠 영역", color = AconTheme.color.White)
                }
            }
        }

        Box(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .width(IntrinsicSize.Min)
        ) {
            AconTag(
                text = stringResource(R.string.store_tag_local),
                backgroundColor = AconTheme.color.TagLocal,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .then(
                        if (isAcornLottieAnimationEnded)
                            Modifier
                                .shadowLayerBackground(
                                    shadowColor = AconTheme.color.TagLocal,
                                    shadowAlpha = localTagAlphaAnimation,
                                    shadowRadius = 100f
                                )
                                .alpha(localTagAlphaAnimation)
                        else Modifier.alpha(0f)
                    ),
                textStyle = AconTheme.typography.Body1.copy(
                    color = AconTheme.color.White,
                    fontWeight = FontWeight.SemiBold
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(137.dp))
    }
}

@Composable
@Preview
private fun IntroduceReviewScreenPreview() {
    IntroduceLocalReviewScreen(
        modifier = Modifier
            .screenDefault()
            .padding(top = 54.dp)
    )
}
