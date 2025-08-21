package com.acon.feature.onboarding.introduce.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.effect.screenDefault
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.ui.base.ScreenProvider
import com.acon.acon.core.ui.compose.getScreenWidth
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

internal class IntroduceTop50ScreenProvider : ScreenProvider {

    @Composable
    override fun provide() {
        IntroduceTop50Screen(
            modifier = Modifier.fillMaxSize().padding(top = 54.dp)
        )
    }
}

private const val UPWARD_IMAGE_MOVING_DISTANCE_ABS_PX = 560f
private const val DOWNWARD_IMAGE_MOVING_DISTANCE_ABS_PX = 240f
private const val IMAGE_ANIMATION_DURATION_MS = 800

@Composable
internal fun IntroduceTop50Screen(
    modifier: Modifier = Modifier
) {

    val screenWidth = getScreenWidth()
    val upwardImageMovingAnimation = remember { Animatable(0f) }
    val downwardImageMovingAnimation = remember { Animatable(0f) }
    val upwardImageAlphaAnimation = remember { Animatable(0f) }

    Column(
        modifier = modifier.verticalScroll(
            state = rememberScrollState(),
            enabled = false
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.introduce_top50_title),
            color = AconTheme.color.White,
            style = AconTheme.typography.Title2,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = stringResource(R.string.introduce_top50_content),
            color = AconTheme.color.White,
            style = AconTheme.typography.Title4,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 24.dp)
        )

        Row(
            modifier = Modifier
                .padding(top = 32.dp)
                .requiredWidth(screenWidth * 1.5f)
                .zIndex(-1f),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.onboarding_spot_samples1),
                contentDescription = null,
                modifier = Modifier.width(screenWidth * 0.4f).offset {
                    IntOffset(x = 0, y = downwardImageMovingAnimation.value.roundToInt())
                },
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopCenter
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(R.drawable.onboarding_spot_samples2),
                contentDescription = null,
                modifier = Modifier.padding(top = 88.dp).width(screenWidth * 0.4f).offset {
                    IntOffset(x = 0, y = upwardImageMovingAnimation.value.roundToInt())
                }.drawWithContent {
                    drawContent()

                    drawRect(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f  to Color.Black,
                                0.16f to Color.Black,
                                0.32f to Color.Black.copy(alpha = 0f)
                            )
                        ),
                        alpha = upwardImageAlphaAnimation.value
                    )
                },
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopCenter
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(R.drawable.onboarding_spot_samples3),
                contentDescription = null,
                modifier = Modifier.width(screenWidth * 0.4f).offset {
                    IntOffset(x = 0, y = downwardImageMovingAnimation.value.roundToInt())
                },
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopCenter
            )
        }
    }

    LaunchedEffect(Unit) {
        launch {
            upwardImageMovingAnimation.animateTo(
                targetValue = -UPWARD_IMAGE_MOVING_DISTANCE_ABS_PX,
                animationSpec = tween(
                    durationMillis = IMAGE_ANIMATION_DURATION_MS,
                    easing = LinearEasing
                )
            )
        }
        launch {
            downwardImageMovingAnimation.animateTo(
                targetValue = DOWNWARD_IMAGE_MOVING_DISTANCE_ABS_PX,
                animationSpec = tween(
                    durationMillis = IMAGE_ANIMATION_DURATION_MS,
                    easing = LinearEasing
                )
            )
        }
        launch {
            upwardImageAlphaAnimation.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = IMAGE_ANIMATION_DURATION_MS,
                    easing = LinearEasing
                )
            )
        }
    }
}

@Composable
@Preview
private fun IntroduceTop50ScreenPreview() {
    IntroduceTop50Screen(
        modifier = Modifier.screenDefault().padding(top = 54.dp)
    )
}