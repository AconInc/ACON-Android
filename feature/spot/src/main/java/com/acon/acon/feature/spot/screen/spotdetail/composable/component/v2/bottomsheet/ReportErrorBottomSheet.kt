package com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.glassmorphism.defaultHazeEffect
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import dev.chrisbanes.haze.HazeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportErrorBottomSheet(
    hazeState: HazeState,
    onDismissRequest:() -> Unit,
    onClickReportError:() -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        contentColor = AconTheme.color.Gray9.copy(alpha = 0.5f),
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        dragHandle = null
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AconTheme.color.Danger) // TODO - 임시 색
                    .defaultHazeEffect(
                        hazeState = hazeState,
                        tintColor = AconTheme.color.GlassWhiteDisabled
                    )
            ) {
                Spacer(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)
                        .clip(CircleShape)
                        .size(width = 36.dp, height = 5.dp)
                        .background(AconTheme.color.Gray500)
                )

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 20.dp, bottom = 70.dp)
                ) {
                    Text(
                        text = "더보기",
                        color = AconTheme.color.White,
                        style = AconTheme.typography.Title3,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .noRippleClickable { onClickReportError() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(
                                R.drawable.ic_error
                            ),
                            contentDescription = "정보 오류 신고하기"
                        )

                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "정보 오류 신고하기",
                            color = AconTheme.color.White,
                            style = AconTheme.typography.Title4
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ReportErrorBottomSheetPreview() {
    AconTheme {
        ReportErrorBottomSheet(
            hazeState = HazeState(),
            onDismissRequest = {},
            onClickReportError = {}
        )
    }
}