package com.acon.acon.feature.profile.composable.screen.profile.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.animation.skeleton
import com.acon.acon.core.designsystem.component.loading.SkeletonItem

@Composable
fun BookmarkSkeletonItem(
    skeletonHeight: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .skeleton(
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        SkeletonItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(skeletonHeight)
                .padding(top = 20.dp)
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(8.dp)
        )
    }
}