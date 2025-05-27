package com.acon.acon.feature.areaverification.v2

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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.component.dialog.AconPermissionDialog
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.utils.feature.action.BackOnPressed

@Composable
internal fun AreaVerificationHomeScreen(
    state: AreaVerificationHomeUiState,
    route: String,
    onNextButtonClick: () -> Unit,
    onPermissionSettingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    val offsetY = (screenHeightDp * 0.65f).dp

    if (state.showPermissionDialog) {
        AconPermissionDialog(
            onPermissionGranted = onPermissionSettingClick
        )
    }

    if(route != stringResource(com.acon.acon.feature.areaverification.R.string.area_verification_settings)) {
        BackOnPressed(context)
    }

    when (state) {
        is AreaVerificationHomeUiState -> {
            Box(
                modifier = modifier
                    .paint(
                        painterResource(R.drawable.area_verification_home_bg),
                        contentScale = ContentScale.FillBounds
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
                        text = "믿을 수 있는 리뷰를 위해\n 지역인증이 필요해요",
                        color = AconTheme.color.White,
                        style = AconTheme.typography.Title1,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "더 정확한 로컬맛집을 추천해드릴 수 있어요",
                        color = AconTheme.color.Gray50,
                        style = AconTheme.typography.Body1
                    )

                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "1초만에 인증하기",
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
                            text = "다음",
                            color = AconTheme.color.White,
                            style = AconTheme.typography.Title4,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AreaVerificationHomeScreenPreview() {
    AconTheme {
        AreaVerificationHomeScreen(
            state = AreaVerificationHomeUiState(),
            route = "",
            onPermissionSettingClick = {},
            onNextButtonClick = {}
        )
    }
}