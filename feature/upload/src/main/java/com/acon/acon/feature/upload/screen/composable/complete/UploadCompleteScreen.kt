package com.acon.acon.feature.upload.screen.composable.complete

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledTextButton
import com.acon.acon.core.designsystem.theme.AconTheme
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

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
        LottieAnimation(
            composition = rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(R.raw.upload_success)
            ).value,
            modifier = Modifier
                .padding(top = 54.dp)
                .fillMaxWidth(.8f),
            iterations = 1,
            isPlaying = true
        )
        Spacer(Modifier.weight(1f))
        AconFilledTextButton(
            text = stringResource(R.string.done),
            onClick = onComplete,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        )
    }
}

@Composable
@Preview
private fun UploadCompleteScreenPreview() {
    UploadCompleteScreen(
        spotName = "장소명",
        onComplete = {},
        modifier = Modifier.fillMaxSize()
    )
}