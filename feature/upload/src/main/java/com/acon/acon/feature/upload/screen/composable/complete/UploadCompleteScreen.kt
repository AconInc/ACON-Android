package com.acon.acon.feature.upload.screen.composable.complete

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledTextButton
import com.acon.acon.core.designsystem.theme.AconTheme
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

private const val SCREEN_CLOSE_SECONDS = 3

@Composable
internal fun UploadCompleteScreen(
    spotName: String,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {

    BackHandler {
        onComplete()
    }

    val displaySpotName = if (spotName.length > 9) {
        spotName.take(9) + "…"
    } else {
        spotName
    }
    var closeInSeconds by remember { mutableIntStateOf(SCREEN_CLOSE_SECONDS) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.review_complete, displaySpotName),
            style = AconTheme.typography.Title2,
            color = AconTheme.color.White,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 80.dp)
        )
        Text(
            text = stringResource(R.string.wish_was_your_spot),
            style = AconTheme.typography.Body1,
            color = AconTheme.color.Gray500,
            fontWeight = FontWeight.W400,
            modifier = Modifier.padding(top = 12.dp)
        )
        Box(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
                .weight(1f)
        ) {
            LottieAnimation(
                composition = rememberLottieComposition(
                    spec = LottieCompositionSpec.RawRes(R.raw.upload_success)
                ).value,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                iterations = 1,
                isPlaying = true
            )
        }
        Text(
            text = stringResource(R.string.tab_closed_in_seconds, closeInSeconds),
            style = AconTheme.typography.Body1,
            color = AconTheme.color.Gray500,
            fontWeight = FontWeight.W400,
        )
        AconFilledTextButton(
            text = stringResource(R.string.done),
            onClick = onComplete,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        )
    }

    LaunchedEffect(Unit) {
        while (closeInSeconds > 0) {
            delay(1_000)
            --closeInSeconds
        }
        onComplete()
    }
}