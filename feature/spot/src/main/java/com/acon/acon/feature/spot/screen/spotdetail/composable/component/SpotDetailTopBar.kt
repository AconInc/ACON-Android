package com.acon.acon.feature.spot.screen.spotdetail.composable.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.type.SpotType
import com.acon.acon.feature.spot.getNameResId
import com.acon.acon.core.designsystem.R

@Composable
fun SpotDetailTopBar(
    storeName: String,
    spotType: SpotType,
    modifier: Modifier = Modifier,
    onLeadingIconClicked: () -> Unit = {},
    leadingIcon: @Composable () -> Unit = {
        Image (
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left_28),
            contentDescription = stringResource(com.acon.acon.feature.spot.R.string.topbar_back_button_description),
            modifier = Modifier
                .padding(vertical = 1.dp)
                .clickable { onLeadingIconClicked() }
        )
    },
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(58.dp))
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 14.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon()
            Text(
                text = storeName,
                style = AconTheme.typography.head5_22_sb,
                color = AconTheme.color.White,
                modifier = Modifier
                    .padding(start = 8.dp, top = 1.dp, bottom = 1.dp)
            )
            Text(
                text = stringResource(spotType.getNameResId()),
                style = AconTheme.typography.subtitle2_14_med,
                color = AconTheme.color.Gray4,
                modifier = Modifier
                    .padding(start = 6.dp, top = 5.dp, bottom = 5.dp)
            )
        }
    }
}


@Composable
@Preview
private fun SpotDetailTopBarPreview(){
    AconTheme {
        SpotDetailTopBar(
            storeName = "냐옹이",
            spotType = SpotType.CAFE
        )
    }
}