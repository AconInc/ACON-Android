package com.acon.acon.feature.upload

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acon.acon.core.designsystem.component.button.AconFilledLargeButton
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.theme.AconTheme
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.acon.acon.feature.upload.R
import kotlinx.coroutines.delay

@Composable
fun UploadSuccessContainer(
    modifier: Modifier = Modifier,
    viewModel: UploadViewModel = hiltViewModel(),
    onNavigateToSpotList: () -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    UploadSuccessScreen(
        modifier = modifier,
        onIntent = viewModel::onIntent,
        onNavigateToSpotList = onNavigateToSpotList
    )
}

@Composable
fun UploadSuccessScreen(
    modifier: Modifier = Modifier,
    onIntent: (UploadIntent) -> Unit,
    onNavigateToSpotList: () -> Unit
) {
    var countdown by remember { mutableIntStateOf(5) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000)
            countdown--
        }
        onNavigateToSpotList()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = AconTheme.color.Gray9)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState)
    ) {
        AconTopBar(
            leadingIcon = {
                IconButton(onClick = onNavigateToSpotList) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = com.acon.acon.core.designsystem.R.drawable.ic_dissmiss_24),
                        contentDescription = "Close",
                    )
                }
            },
            content = {
                Text(
                    text = "업로드",
                    style = AconTheme.typography.head5_22_sb,
                    color = AconTheme.color.White
                )
            }
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "가게명에 대한",
                    style = AconTheme.typography.head6_20_sb,
                    color = AconTheme.color.White,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = "리뷰 작성을 완료했어요!",
                    style = AconTheme.typography.head6_20_sb,
                    color = AconTheme.color.White,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "당신의 취향이 가득 담긴 장소길 바라요",
                    style = AconTheme.typography.body3_13_reg,
                    color = AconTheme.color.Gray3
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.85f),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition = rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(R.raw.upload_success)
                    ).value,
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1f),
                    iterations = 1,
                    isPlaying = true
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "${countdown}초 후 자동으로 닫힙니다.",
                    style = AconTheme.typography.body3_13_reg,
                    color = AconTheme.color.Gray3,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))
                AconFilledLargeButton(
                    text = "확인",
                    textStyle = AconTheme.typography.head8_16_sb,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
                    disabledBackgroundColor = AconTheme.color.Gray7,
                    enabledBackgroundColor = AconTheme.color.Gray7,
                    onClick = onNavigateToSpotList
                )
            }
        }
    }
}

@Preview
@Composable
private fun UploadSuccessScreenPreview() {
    AconTheme {
        UploadSuccessScreen(
            modifier = Modifier,
            onIntent = {},
            onNavigateToSpotList = {}
        )
    }
}
