package com.acon.acon.feature.spot.screen.spotdetail.composable.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun SpotChip(
    selected: Boolean,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = AconTheme.typography.subtitle2_14_med,
) {
    val containerColor =
        if (selected)
            AconTheme.color.Main_org35
        else AconTheme.color.Gray6

    val textColor =
        if (selected)
            AconTheme.color.White
        else AconTheme.color.Gray5

    val title =
        if(selected) stringResource(com.acon.acon.feature.spot.R.string.after_business)
        else stringResource(com.acon.acon.feature.spot.R.string.before_sales)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(containerColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 14.dp),
            text = title,
            color = textColor,
            style = textStyle
        )
    }
}

@Preview
@Composable
private fun SpotChipPreview() {
    SpotChip(
        selected = false,
    )
}

@Preview
@Composable
private fun SelectedSpotChipPreview() {
    SpotChip(
        selected = true,
    )
}
