package com.acon.acon.feature.upload.screen.composable.complete

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UploadCompleteScreenContainer(
    spotName: String,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    UploadCompleteScreen(
        spotName = spotName,
        onComplete = onNavigateToHome,
        modifier = modifier
    )
}