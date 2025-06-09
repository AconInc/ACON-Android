package com.acon.acon.feature.spot.screen.spotdetail.composable

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
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
                text = stringResource(R.string.see_more),
                color = AconTheme.color.White,
                style = AconTheme.typography.Title3,
                fontWeight = FontWeight.SemiBold,
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
                        R.drawable.ic_error_report
                    ),
                    contentDescription = stringResource(R.string.complaint_content_description)
                )

                Spacer(Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.complaint_content_description),
                    color = AconTheme.color.White,
                    style = AconTheme.typography.Title4,
                    fontWeight = FontWeight.Normal
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