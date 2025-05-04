package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.blur.LocalHazeState
import com.acon.acon.core.designsystem.blur.defaultHazeEffect
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.type.SpotType
import com.acon.acon.feature.spot.mock.spotListUiStateMock
import com.acon.acon.feature.spot.screen.component.SpotTypeToggle
import com.acon.acon.feature.spot.screen.spotlist.SpotListUiStateV2
import dev.chrisbanes.haze.hazeSource

@Composable
internal fun SpotListScreenV2(
    state: SpotListUiStateV2,
    onSpotTypeChanged: (SpotType) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier.hazeSource(LocalHazeState.current)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(bottom = 14.dp)
                .defaultHazeEffect(
                    hazeState = LocalHazeState.current,
                    tintColor = AconTheme.color.Gray900,
                    blurRadius = 20.dp
                ),
        ) {
            SpotTypeToggle(
                selectedType = (state as? SpotListUiStateV2.Success)?.selectedSpotType ?: SpotType.RESTAURANT,
                onSwitched = onSpotTypeChanged,
                modifier = Modifier.align(Alignment.Center)
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_filter),
                contentDescription = stringResource(R.string.filter_content_description),
                tint = AconTheme.color.Gray50,
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp).clickable {
                    // TODO("Filter Clicked")
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .hazeSource(LocalHazeState.current)
                .verticalScroll(rememberScrollState())
        ) {

        }
    }
}

@Composable
@Preview
private fun SpotListScreenV2Preview() {
    SpotListScreenV2(
        state = spotListUiStateMock,
        onSpotTypeChanged = {},
        modifier = Modifier
            .fillMaxWidth()
            .background(AconTheme.color.Gray900)
            .width(400.dp)
    )
}