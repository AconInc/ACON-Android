package com.acon.acon.core.designsystem.size

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun getScreenWidth(): Dp = LocalConfiguration.current.screenWidthDp.dp

@Composable
fun getScreenHeight(): Dp = LocalConfiguration.current.screenHeightDp.dp