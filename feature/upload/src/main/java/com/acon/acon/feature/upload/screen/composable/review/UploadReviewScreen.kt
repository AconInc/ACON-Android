package com.acon.acon.feature.upload.screen.composable.review

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledTextButton
import com.acon.acon.core.designsystem.effect.effect.shadowLayerBackground
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.upload.screen.UploadReviewUiState
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val MAX_ACORN_COUNT = 5

@Composable
internal fun UploadReviewScreen(
    state: UploadReviewUiState,
    onBackAction: () -> Unit,
    onCompleteAction: () -> Unit,
    onAcornClick: (count: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(state is UploadReviewUiState.Success)

    val isCompleteButtonEnabled by remember(state) {
        derivedStateOf {
            state.selectedAcornCount in 1..MAX_ACORN_COUNT
        }
    }

    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    var hapticJob by remember { mutableStateOf<Job?>(null) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 21.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_left),
                contentDescription = stringResource(R.string.back),
                tint = AconTheme.color.Gray50,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .noRippleClickable {
                        onBackAction()
                    }
            )

            Text(
                text = stringResource(R.string.title_upload),
                style = AconTheme.typography.Title4,
                color = AconTheme.color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Text(
            text = state.spot.name,
            style = AconTheme.typography.Title3,
            color = AconTheme.color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 37.dp)
        )
        Text(
            text = stringResource(R.string.introduce_drop_acorn),
            style = AconTheme.typography.Title5,
            color = AconTheme.color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp)
        )

        Row(modifier = Modifier.padding(top = 24.dp)) {
            for(i in 1..MAX_ACORN_COUNT) {
                AcornIndicator(
                    isSelected = state.selectedAcornCount >= i,
                    onClick = {
                        if (state.selectedAcornCount != i) {
                            hapticJob?.cancel()
                            hapticJob = scope.launch(Dispatchers.Main.immediate) {
                                delay(470)
                                repeat(minOf(i,3)) {
                                    launch(Dispatchers.Main.immediate) {
                                        repeat(4) {
                                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                            delay(590)
                                        }
                                    }
                                    delay(240L)
                                }
                            }
                            onAcornClick(i)
                        }
                    },
                )
            }
        }

        Text(
            text = stringResource(R.string.introduce_click_acorn),
            style = AconTheme.typography.Body1,
            color = AconTheme.color.Gray500,
            fontWeight = FontWeight.W400,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "${state.selectedAcornCount}/$MAX_ACORN_COUNT",
            style = AconTheme.typography.Body1,
            color = AconTheme.color.Gray500,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 26.dp, bottom = 20.dp),
            contentAlignment = Alignment.Center
        ) {

            when(state.selectedAcornCount) {
                1 -> AcornLottieAnimation(1)
                2 -> AcornLottieAnimation(2)
                3 -> AcornLottieAnimation(3)
                4 -> AcornLottieAnimation(4)
                5 -> AcornLottieAnimation(5)
            }
        }

        Box(
            modifier = Modifier.height(IntrinsicSize.Min),
            contentAlignment = Alignment.Center
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp)
                    .then(
                        if (state.selectedAcornCount > 0) Modifier.shadowLayerBackground(
                            shadowColor = AconTheme.color.PrimaryDefault,
                            shadowAlpha = .6f,
                            shadowRadius = 1000f,
                        ) else Modifier
                    )
            )

            AconFilledTextButton(
                text = stringResource(R.string.complete_drop_acorn),
                onClick = onCompleteAction,
                enabled = isCompleteButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun AcornLottieAnimation(
    acornCount: Int,
) {
    LottieAnimation(
        composition = rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(
                when (acornCount) {
                    1 -> R.raw.acorn1
                    2 -> R.raw.acorn2
                    3 -> R.raw.acorn3
                    4 -> R.raw.acorn4
                    5 -> R.raw.acorn5
                    else -> 0
                }
            )
        ).value,
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
    )
}