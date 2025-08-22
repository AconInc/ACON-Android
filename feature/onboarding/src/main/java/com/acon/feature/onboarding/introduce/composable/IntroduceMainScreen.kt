package com.acon.feature.onboarding.introduce.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.animation.fadeIn
import com.acon.acon.core.designsystem.animation.slidingFadeIn
import com.acon.acon.core.designsystem.component.button.v2.AconFilledTextButton
import com.acon.acon.core.designsystem.effect.screenDefault
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.ui.base.ScreenProvider

internal class IntroduceMainScreenProvider(
    private val onStartButtonClick: () -> Unit,
    private val onDisposed: () -> Unit,
    private val animationEnabled: () -> Boolean
) : ScreenProvider {

    @Composable
    override fun provide() {
        IntroduceMainScreen(
            onStartButtonClick = onStartButtonClick,
            modifier = Modifier.padding(top = 54.dp).navigationBarsPadding(),
            animationEnabled = animationEnabled
        )
        DisposableEffect(Unit) {

            onDispose {
                onDisposed()
            }
        }
    }
}

private const val COMMON_TWEEN_DELAY = 200
private const val COMMON_APPEAR_ANIMATION_DURATION_MS = 500

@Composable
internal fun IntroduceMainScreen(
    onStartButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    animationEnabled: () -> Boolean = { true }
) {

    if (animationEnabled())
        AnimationEnabledIntroduceMainScreen(
            modifier = modifier,
            onStartButtonClick = onStartButtonClick
        )
    else
        AnimationDisabledIntroduceMainScreen(
            modifier = modifier,
            onStartButtonClick = onStartButtonClick
        )
}

@Composable
private fun AnimationEnabledIntroduceMainScreen(
    onStartButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val imageAppearDelayMillis = COMMON_TWEEN_DELAY
    val imageAppearDurationMillis = COMMON_APPEAR_ANIMATION_DURATION_MS

    val titleAppearDelayMillis = imageAppearDelayMillis + imageAppearDurationMillis + COMMON_TWEEN_DELAY
    val titleAppearDurationMillis = COMMON_APPEAR_ANIMATION_DURATION_MS

    val messageAppearDelayMillis = titleAppearDelayMillis + titleAppearDurationMillis
    val messageAppearDurationMillis = COMMON_APPEAR_ANIMATION_DURATION_MS

    val startButtonAppearDelayMillis = messageAppearDelayMillis + messageAppearDurationMillis + COMMON_TWEEN_DELAY
    val startButtonAppearDurationMillis = COMMON_APPEAR_ANIMATION_DURATION_MS

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box() {
            Column {
                Image(
                    painter = painterResource(R.drawable.onboarding_main_spot_sample),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .slidingFadeIn(
                            delayMillis = COMMON_TWEEN_DELAY,
                            durationMillis = COMMON_APPEAR_ANIMATION_DURATION_MS
                        )
                )
                Spacer(Modifier.height(40.dp))
            }
            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.introduce_main_title),
                    color = AconTheme.color.White,
                    style = AconTheme.typography.Title2,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.slidingFadeIn(
                        delayMillis = titleAppearDelayMillis,
                        durationMillis = titleAppearDurationMillis
                    )
                )
                Text(
                    text = stringResource(R.string.introduce_main_content),
                    color = AconTheme.color.White,
                    style = AconTheme.typography.Title4,
                    modifier = Modifier.padding(top = 24.dp).slidingFadeIn(
                        delayMillis = messageAppearDelayMillis,
                        durationMillis = messageAppearDurationMillis
                    )
                )
            }
        }
        Spacer(Modifier.weight(1f))
        AconFilledTextButton(
            text = stringResource(R.string.start),
            onClick = onStartButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .fadeIn(
                    delayMillis = startButtonAppearDelayMillis,
                    durationMillis = startButtonAppearDurationMillis
                )
        )
    }
}
@Composable
private fun AnimationDisabledIntroduceMainScreen(
    onStartButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box() {
            Column {
                Image(
                    painter = painterResource(R.drawable.onboarding_main_spot_sample),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                )
                Spacer(Modifier.height(40.dp))
            }
            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.introduce_main_title),
                    color = AconTheme.color.White,
                    style = AconTheme.typography.Title2,
                    fontWeight = FontWeight.ExtraBold,
                )
                Text(
                    text = stringResource(R.string.introduce_main_content),
                    color = AconTheme.color.White,
                    style = AconTheme.typography.Title4,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        }
        Spacer(Modifier.weight(1f))
        AconFilledTextButton(
            text = stringResource(R.string.start),
            onClick = onStartButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
@Preview
private fun IntroduceMainScreenPreview() {
    IntroduceMainScreen(
        onStartButtonClick = {},
        modifier = Modifier.screenDefault().padding(top = 11.dp),
        animationEnabled = { false }
    )
}