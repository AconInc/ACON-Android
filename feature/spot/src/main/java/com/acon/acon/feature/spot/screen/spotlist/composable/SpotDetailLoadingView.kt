package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.component.loading.SkeletonItem

@Composable
internal fun SpotDetailLoadingView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkeletonItem(
                modifier = Modifier.size(width = 210.dp, height = 24.dp)
            )

            Spacer(Modifier.weight(1f))
            SkeletonItem(
                modifier = Modifier.size(width = 24.dp, height = 24.dp)
            )

            SkeletonItem(
                modifier = Modifier.size(width = 44.dp, height = 20.dp)
            )
        }

        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.padding(start = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkeletonItem(
                modifier = Modifier
                    .padding(vertical = 3.dp, horizontal = 17.dp)
            )

            SkeletonItem(
                modifier = Modifier
                    .padding(vertical = 3.dp, horizontal = 17.dp)
            )

            SkeletonItem(
                modifier = Modifier
                    .padding(vertical = 3.dp, horizontal = 17.dp)
            )
        }

        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                SkeletonItem(
                    modifier = Modifier.size(width = 166.dp, height = 24.dp)
                )

                Spacer(Modifier.height(12.dp))
                SkeletonItem(
                    modifier = Modifier.size(width = 166.dp, height = 24.dp)
                )
            }
        }

        Column(
            modifier = Modifier.padding(end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(36.dp)
        ) {
            SkeletonItem(
                modifier = Modifier.size(width = 36.dp, height = 58.dp)
            )

            SkeletonItem(
                modifier = Modifier.size(width = 36.dp, height = 58.dp)
            )

            SkeletonItem(
                modifier = Modifier.size(width = 36.dp, height = 58.dp)
            )
        }

        SkeletonItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp)
        )
    }
}