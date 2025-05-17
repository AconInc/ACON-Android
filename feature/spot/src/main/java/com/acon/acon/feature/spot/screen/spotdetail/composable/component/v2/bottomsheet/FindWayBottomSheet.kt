package com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2.bottomsheet

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.component.bottomsheet.AconBottomSheet
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FindWayBottomSheet(
    onFindWay: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AconBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 31.dp)
                .noRippleClickable { onFindWay() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "네이버지도",
                color = AconTheme.color.White,
                style = AconTheme.typography.Title4,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun FindWayBottomSheetPreview() {
    AconTheme {
        FindWayBottomSheet(
            onFindWay = {},
            onDismissRequest = {}
        )
    }
}