package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.spot.v2.SpotV2
import com.acon.acon.feature.spot.mock.spotListUiStateRestaurantMock
import com.acon.feature.common.compose.toDp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun SpotEmptyView(
    otherSpots: ImmutableList<SpotV2>,
    onSpotClick: (SpotV2) -> Unit,
    onTryFindWay: (SpotV2) -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val screenHeightPx = with(LocalDensity.current) {
        screenHeightDp.toPx()
    }

    val itemHeightPx by remember {
        mutableFloatStateOf(screenHeightPx * .3f)
    }

    LazyColumn(
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 24.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        item {
            Text(
                text = stringResource(R.string.no_available_spots),
                style = AconTheme.typography.Title2,
                color = AconTheme.color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 24.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.apologize_for_no_spots),
                style = AconTheme.typography.Body1,
                color = AconTheme.color.Gray500,
                fontWeight = FontWeight.W400,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        if (otherSpots.isNotEmpty()) {
            itemsIndexed(
                items = otherSpots
            ) { index, spot ->
                if (index == 0)
                    Text(
                        text = stringResource(R.string.recommend_other_spots),
                        style = AconTheme.typography.Title4,
                        color = AconTheme.color.White,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 60.dp, bottom = 24.dp)
                    )
                SpotItem(
                    spot = spot,
                    onItemClick = onSpotClick,
                    onFindWayButtonClick = onTryFindWay,
                    modifier = Modifier
                        .height(itemHeightPx.toDp())
                        .padding(bottom = 12.dp)
                        .fillMaxWidth()
                )
            }
        } else {
            item {
                Text(
                    text = stringResource(R.string.no_other_spots),
                    style = AconTheme.typography.Title4,
                    color = AconTheme.color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 60.dp, bottom = 24.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.ask_for_new_spot),
                    style = AconTheme.typography.Body1,
                    color = AconTheme.color.Action,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SpotListEmptyView1Preview() {
    SpotEmptyView(
        otherSpots = spotListUiStateRestaurantMock.spotList.toImmutableList(),
        onSpotClick = {},
        onTryFindWay = {},
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
private fun SpotListEmptyView2Preview() {
    SpotEmptyView(
        otherSpots = listOf<SpotV2>().toImmutableList(),
        onSpotClick = {},
        onTryFindWay = {},
        modifier = Modifier.fillMaxSize()
    )
}