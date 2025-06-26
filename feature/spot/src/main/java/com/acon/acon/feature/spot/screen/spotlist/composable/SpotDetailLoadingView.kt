package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.component.loading.SkeletonItem
import com.acon.feature.common.compose.getScreenHeight

@Composable
internal fun SpotDetailLoadingView(
    modifier: Modifier = Modifier
) {
    val screenHeightDp = getScreenHeight()
    val offsetY = (screenHeightDp * 0.6f)

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 66.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkeletonItem(
                modifier = Modifier
                    .weight(8f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
            )

            SkeletonItem(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .padding(start = 8.dp)
                    .padding(vertical = 3.dp)
            )
        }

        Spacer(Modifier.height(offsetY))
        SkeletonItem(
            modifier = Modifier
                .size(width = 60.dp, height = 24.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Column(
            modifier = Modifier
                .width(242.dp)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SkeletonItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            SkeletonItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            SkeletonItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        Spacer(modifier = Modifier.height(50.dp))
        SkeletonItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    }
}