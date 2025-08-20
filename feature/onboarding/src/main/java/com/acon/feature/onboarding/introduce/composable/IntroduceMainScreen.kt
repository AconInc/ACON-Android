package com.acon.feature.onboarding.introduce.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledTextButton
import com.acon.acon.core.designsystem.effect.screenDefault
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.ui.base.ScreenProvider

internal class IntroduceMainScreenProvider(
    private val onStartButtonClick: () -> Unit
) : ScreenProvider {

    @Composable
    override fun provide() {
        IntroduceMainScreen(
            onStartButtonClick = onStartButtonClick,
            modifier = Modifier.padding(top = 11.dp)
        )
    }
}

@Composable
internal fun IntroduceMainScreen(
    onStartButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.onboarding_main_spot_sample),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
            )
            Column(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(
                    text = stringResource(R.string.introduce_main_title),
                    color = AconTheme.color.White,
                    style = AconTheme.typography.Title2,
                    fontWeight = FontWeight.ExtraBold,
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        Text(
            text = stringResource(R.string.introduce_main_content),
            color = AconTheme.color.White,
            style = AconTheme.typography.Title4,
            modifier = Modifier.offset {
                IntOffset(x = 0, y = -16)
            }
        )
        Spacer(modifier = Modifier.weight(1f))
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
        modifier = Modifier.screenDefault().padding(top = 11.dp)
    )
}