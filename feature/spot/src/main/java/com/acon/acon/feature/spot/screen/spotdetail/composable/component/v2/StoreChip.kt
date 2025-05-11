package com.acon.acon.feature.spot.screen.spotdetail.composable.component.v2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun StoreChip(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = AconTheme.typography.Caption1.copy(
        color = AconTheme.color.White
    )
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(AconTheme.color.New),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 16.dp),
            text = "NEW",
            style = textStyle
        )
    }
}

@Preview
@Composable
private fun StoreChipPreview() {
    AconTheme {
        StoreChip()
    }
}