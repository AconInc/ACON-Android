package com.acon.acon.core.designsystem.component.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import com.acon.acon.core.designsystem.animation.Skeleton
import com.acon.acon.core.designsystem.animation.skeleton

@Composable
fun SkeletonItem(
    modifier: Modifier = Modifier,
    brush: Brush = Skeleton.brush,
    shape: Shape = Skeleton.shape
) {

    Box(
        modifier = modifier.skeleton(
            brush = brush,
            shape = shape
        )
    )
}