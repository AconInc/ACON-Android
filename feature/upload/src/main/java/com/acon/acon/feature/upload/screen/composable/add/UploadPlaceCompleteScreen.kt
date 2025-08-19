package com.acon.acon.feature.upload.screen.composable.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.theme.AconTheme
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
internal fun UploadPlaceCompleteScreen(
    onClickGoHome: () -> Unit,
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.upload_success)
    )
    var isPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(500)
        isPlaying = true
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AconTheme.color.Gray900)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.upload_place_complete_title),
            style = AconTheme.typography.Headline3,
            color = AconTheme.color.White,
            modifier = Modifier
                .padding(top = 50.dp)
        )

        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.upload_place_complete_sub_title),
            style = AconTheme.typography.Title5,
            color = AconTheme.color.Gray500,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(2.dp)
        )

        LottieAnimation(
            composition = composition,
            modifier = Modifier
                .padding(top = 54.dp)
                .fillMaxWidth(.8f),
            iterations = 1,
            isPlaying = true
        )

        Spacer(Modifier.weight(1f))
        AconFilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            onClick = { onClickGoHome() },
            content = {
                Text(
                    text = stringResource(R.string.go_home),
                    style = AconTheme.typography.Title4,
                    fontWeight = FontWeight.SemiBold
                )
            }
        )
    }
}

@Preview
@Composable
private fun UploadPlaceCompleteScreenPreview() {
    AconTheme {
        UploadPlaceCompleteScreen(
            onClickGoHome = {}
        )
    }
}