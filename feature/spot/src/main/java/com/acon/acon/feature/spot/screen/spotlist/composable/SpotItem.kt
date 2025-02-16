package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.spot.Spot
import com.acon.acon.domain.type.SpotType
import com.acon.acon.feature.spot.getNameResId
import com.acon.acon.feature.spot.R

@Composable
fun SpotItem(
    spot: Spot,
    isFirstRank: Boolean,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
    ) {
        if (LocalInspectionMode.current) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AconTheme.color.Dim_gra_2)
            )
        } else {
            AsyncImage(
                model = spot.image,
                contentDescription = spot.name,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AconTheme.color.Dim_gra_2)
            )
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (spot.matchingRate >= 0)
                Text(
                    text = stringResource(R.string.matching_rate, spot.matchingRate.toString().padStart(2, '0')),
                    style = AconTheme.typography.subtitle2_14_med,
                    color = AconTheme.color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isFirstRank) AconTheme.color.Gray9 else AconTheme.color.Gla_w_20)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(spot.type.getNameResId()),
                style = AconTheme.typography.subtitle2_14_med,
                color = AconTheme.color.White
            )
            Text(
                text = spot.name,
                style = AconTheme.typography.title2_20_b,
                color = AconTheme.color.White
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.ic_walking_w_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = AconTheme.color.Gray3
                )
                Text(
                    text = stringResource(R.string.walking_time, spot.walkingTime.toString().padStart(2, ' ')),
                    style = AconTheme.typography.subtitle2_14_med,
                    color = AconTheme.color.Gray3
                )
            }
        }
    }
}

@Preview
@Composable
private fun SpotItemPreview() {
    SpotItem(
        spot = Spot(
            id = 0,
            image = "",
            matchingRate = 87,
            type = SpotType.CAFE,
            name = "메가커피",
            walkingTime = 8
        ), isFirstRank = false
    )
}