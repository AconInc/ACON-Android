package com.acon.acon.feature.upload.v2.composable.review

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledTextButton
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.upload.component.DotoriIndicator
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

private const val MAX_DOTORI_COUNT = 5

@Composable
internal fun UploadReviewScreen(
    state: UploadReviewUiState,
    onBackAction: () -> Unit,
    onCompleteAction: () -> Unit,
    onDotoriClick: (count: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(state is UploadReviewUiState.Success)

    val isCompleteButtonEnabled by remember(state) {
        derivedStateOf {
            state.selectedDotoriCount in 1..MAX_DOTORI_COUNT
        }
    }

    Column(
        modifier = modifier.systemBarsPadding(),
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
            text = stringResource(R.string.introduce_drop_dotori),
            style = AconTheme.typography.Title2,
            color = AconTheme.color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 35.dp)
        )

        Row(modifier = Modifier.padding(top = 24.dp)) {
            for(i in 1..MAX_DOTORI_COUNT) {
                DotoriIndicator(
                    isSelected = state.selectedDotoriCount >= i,
                    onClick = {
                        onDotoriClick(i)
                    },
                )
            }
        }

        Text(
            text = stringResource(R.string.introduce_click_dotori),
            style = AconTheme.typography.Body1,
            color = AconTheme.color.Gray500,
            fontWeight = FontWeight.W400,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "${state.selectedDotoriCount}/$MAX_DOTORI_COUNT",
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

            when(state.selectedDotoriCount) {
                1 -> DotoriLottieAnimation(1)
                2 -> DotoriLottieAnimation(2)
                3 -> DotoriLottieAnimation(3)
                4 -> DotoriLottieAnimation(4)
                5 -> DotoriLottieAnimation(5)
            }
        }

        AconFilledTextButton(
            text = stringResource(R.string.complete_drop_dotori),
            onClick = onCompleteAction,
            enabled = isCompleteButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
private fun DotoriLottieAnimation(
    dotoriCount: Int,
) {
    LottieAnimation(
        composition = rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(
                when (dotoriCount) {
                    1 -> R.raw.dotori1
                    2 -> R.raw.dotori2
                    3 -> R.raw.dotori3
                    4 -> R.raw.dotori4
                    5 -> R.raw.dotori5
                    else -> 0
                }
            )
        ).value,
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
    )
}