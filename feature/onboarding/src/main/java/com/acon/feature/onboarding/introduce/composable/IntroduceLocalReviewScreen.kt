package com.acon.feature.onboarding.introduce.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.tag.AconTag
import com.acon.acon.core.designsystem.effect.screenDefault
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.ui.base.ScreenProvider
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

internal class IntroduceLocalReviewScreenProvider : ScreenProvider {

    @Composable
    override fun provide() {
        IntroduceLocalReviewScreen(
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}

@Composable
internal fun IntroduceLocalReviewScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
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
            modifier = Modifier.padding(top = 24.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            if (LocalInspectionMode.current.not())
                LottieAnimation(
                    composition = rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(R.raw.acorn5)
                    ).value,
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

        AconTag(
            text = stringResource(R.string.store_tag_local),
            backgroundColor = AconTheme.color.TagLocal,
            modifier = Modifier.padding(top = 40.dp)
        )

        Spacer(modifier = Modifier.height(137.dp))
    }
}

@Composable
@Preview
private fun IntroduceReviewScreenPreview() {
    IntroduceLocalReviewScreen(
        modifier = Modifier.screenDefault().padding(top = 20.dp)
    )
}