package com.acon.acon.feature.areaverification.composable

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.feature.common.compose.getScreenHeight

@Composable
internal fun AreaVerificationScreen(
    state: AreaVerificationUiState,
    route: String,
    onNextButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenHeightDp = getScreenHeight()
    val offsetY = (screenHeightDp * 0.65f)

    BackHandler {  }

    Box(
        modifier = modifier
            .paint(
                painterResource(R.drawable.area_verification_home_bg),
                contentScale = ContentScale.FillWidth
            )
    ) {
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
            Text(
                text = stringResource(R.string.area_verification_one_second_verify),
                color = AconTheme.color.Gray500,
                style = AconTheme.typography.Body1,
            )

            AconFilledButton(
                onClick = { onNextButtonClick() },
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
    AconTheme {
        AreaVerificationScreen(
            state = AreaVerificationUiState(),
            route = "",
            onNextButtonClick = {}
        )
    }
}