package com.acon.acon.feature.spot.screen.spotdetail.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.acon.acon.core.designsystem.blur.LocalHazeState
import com.acon.acon.core.designsystem.blur.defaultHazeEffect
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun SpotDetailScreenV2() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = AconTheme.color.GlassWhiteDefault
            )
            .defaultHazeEffect(
                hazeState = LocalHazeState.current,
                tintColor = AconTheme.color.Gray200
            )
    ) {
        Text(
            text = "장소 이미지를 준비하고 있어요",
            color = AconTheme.color.Gray200,
        )
    }
}