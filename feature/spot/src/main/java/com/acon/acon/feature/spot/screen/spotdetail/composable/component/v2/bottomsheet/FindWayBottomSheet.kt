package com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import dev.chrisbanes.haze.HazeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindWayBottomSheet(
    hazeState: HazeState,
    onDismissRequest:() -> Unit,
    onFindWay:() -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        contentColor = AconTheme.color.Gray9.copy(alpha = 0.5f), // TODO - 임시 색
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
    }
}

@Preview
@Composable
private fun FindWayBottomSheetPreview() {
    AconTheme {
        FindWayBottomSheet(
            hazeState = HazeState(),
            onDismissRequest = {},
            onFindWay = {}
        )
    }
}