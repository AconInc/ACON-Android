package com.acon.feature.onboarding.area.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.ui.compose.getScreenHeight
import com.acon.feature.onboarding.area.viewmodel.AreaVerificationState

@Composable
internal fun AreaVerificationScreen(
    state: AreaVerificationState,
    onNextButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSkipClick: () -> Unit = {}
) {
    val screenHeightDp = getScreenHeight()
    val offsetY = (screenHeightDp * 0.65f)

    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val skipAlertTextAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "alpha"
    )

    Box(
        modifier = modifier
            .paint(
                painterResource(R.drawable.area_verification_home_bg),
                contentScale = ContentScale.FillWidth
            )
    ) {
        if (state.shouldShowSkipButton) {
            Text(
                text = stringResource(R.string.skip_area_verification),
                style = AconTheme.typography.Body1,
                color = AconTheme.color.White,
                fontWeight = FontWeight.W400,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        end = 16.dp, top = 10.dp
                    )
                    .noRippleClickable {
                        onSkipClick()
                    }
                    .padding(8.dp)
            )
        }

        Text(
            text = stringResource(R.string.alert_about_skip_area_verification),
            style = AconTheme.typography.Body1,
            color = AconTheme.color.Gray500,
            fontWeight = FontWeight.W400,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    top = 96.dp
                )
                .graphicsLayer {
                    alpha = skipAlertTextAlpha
                },
            textAlign = TextAlign.Center
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(offsetY))
            Text(
                text = stringResource(R.string.area_verification_main_title),
                color = AconTheme.color.White,
                style = AconTheme.typography.Title1,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.area_verification_main_subtitle),
                color = AconTheme.color.Gray50,
                style = AconTheme.typography.Body1
            )

            Spacer(Modifier.weight(1f))

            AconFilledButton(
                onClick = onNextButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 20.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.next),
                    color = AconTheme.color.White,
                    style = AconTheme.typography.Title4,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview
@Composable
private fun AreaVerificationHomeScreenPreview() {
    AreaVerificationScreen(
        modifier = Modifier
            .fillMaxSize()
            .background(AconTheme.color.Gray900)
            .statusBarsPadding(),
        onNextButtonClick = {},
        state = AreaVerificationState(shouldShowSkipButton = true)
    )
}