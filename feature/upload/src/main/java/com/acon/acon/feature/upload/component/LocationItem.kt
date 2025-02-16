package com.acon.acon.feature.upload.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.upload.SpotListItem
import com.acon.acon.domain.type.SpotType

@Composable
fun LocationItem(
    locationItem: SpotListItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 7.dp)
            .noRippleClickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = locationItem.name,
                style = AconTheme.typography.subtitle2_14_med,
                color = AconTheme.color.White
            )

            Spacer(modifier = Modifier.padding(top = 4.dp))
            Text(
                text = locationItem.address,
                style = AconTheme.typography.cap1_11_reg,
                color = AconTheme.color.Gray4
            )
        }

        Text(
            text = when (locationItem.spotType) {
                SpotType.CAFE.toString() -> "카페"
                SpotType.RESTAURANT.toString() -> "음식점"
                else -> ""
            },
            style = AconTheme.typography.body3_13_reg,
            color = AconTheme.color.Gray4
        )
    }
}
