package com.acon.acon.feature.upload.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun DotoriIndicator(
    index: Int,
    isSelected: Boolean,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clickable { onClick(index) }
    ) {
        Image(
            imageVector = ImageVector.vectorResource(
                id = if (isSelected) {
                    com.acon.acon.core.designsystem.R.drawable.ic_review_w_40
                } else {
                    com.acon.acon.core.designsystem.R.drawable.ic_review_g_40
                }
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1C1E)
@Composable
private fun DotoriIndicatorPreview() {
    AconTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DotoriIndicator(
                index = 0,
                isSelected = true,
                onClick = {}
            )
            DotoriIndicator(
                index = 1,
                isSelected = false,
                onClick = {}
            )
        }
    }
}
