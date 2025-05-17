package com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.bottomsheet.AconBottomSheet
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReportErrorBottomSheet(
    onDismissRequest: () -> Unit,
    onClickReportError: () -> Unit,
) {
    AconBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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

@Preview
@Composable
private fun ReportErrorBottomSheetPreview() {
    AconTheme {
        ReportErrorBottomSheet(
            onDismissRequest = {},
            onClickReportError = {}
        )
    }
}