package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.animation.skeleton
import com.acon.acon.core.designsystem.component.loading.SkeletonItem
import com.acon.feature.common.compose.toDp

@Composable
internal fun SpotListLoadingView(
    itemHeightPx: Float,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SkeletonItem(
            modifier = Modifier
                .width(120.dp)
                .height(28.dp),
            shape = RoundedCornerShape(8.dp),
        )
        repeat(10) {
            Spacer(modifier = Modifier.height(14.dp))
            Column(
                modifier = Modifier
                    .height(itemHeightPx.toDp())
                    .fillMaxWidth()
                    .skeleton(
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(26.dp)
                ) {
                    SkeletonItem(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(3.5f),
                        shape = RoundedCornerShape(8.dp),
                    )
                    SkeletonItem(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxHeight()
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp),
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                SkeletonItem(
                    modifier = Modifier
                        .height(36.dp)
                        .width(140.dp)
                        .align(Alignment.End),
                    shape = RoundedCornerShape(8.dp),
                )
            }
        }
    }
}