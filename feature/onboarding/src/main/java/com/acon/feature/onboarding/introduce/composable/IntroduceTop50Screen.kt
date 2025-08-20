package com.acon.feature.onboarding.introduce.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.effect.screenDefault
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.ui.base.ScreenProvider
import com.acon.acon.core.ui.compose.getScreenWidth

internal class IntroduceTop50ScreenProvider : ScreenProvider {

    @Composable
    override fun provide() {
        IntroduceTop50Screen(
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}

@Composable
internal fun IntroduceTop50Screen(
    modifier: Modifier = Modifier
) {

    val screenWidth = getScreenWidth()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
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
            modifier = Modifier.padding(top = 24.dp)
        )

        Row(
            modifier = Modifier.fillMaxHeight().padding(top = 32.dp).requiredWidth(screenWidth * 1.2f),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.onboarding_spot_samples1),
                contentDescription = null,
                modifier = Modifier.width(screenWidth * 0.333f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(R.drawable.onboarding_spot_samples2),
                contentDescription = null,
                modifier = Modifier.padding(top = 88.dp).width(screenWidth * 0.333f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(R.drawable.onboarding_spot_samples3),
                contentDescription = null,
                modifier = Modifier.width(screenWidth * 0.333f)
            )
        }
    }
}

@Composable
@Preview
private fun IntroduceTop50ScreenPreview() {
    IntroduceTop50Screen(
        modifier = Modifier.screenDefault().padding(top = 20.dp)
    )
}