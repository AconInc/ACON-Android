package com.acon.android.feature.spot.screen.spotdetail.composable.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.android.core.designsystem.component.button.AconFilledLargeButton
import com.acon.android.core.designsystem.theme.AconTheme
import com.acon.android.core.designsystem.R

@Composable
fun RestaurantBottomActionBar(
    localAcornCount: Int,
    basicAcornCount: Int,
    onClickFindDirections: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
       modifier = modifier
           .fillMaxWidth()
           .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp)
    ) {
        AconIconAndCount(
            aconIcon =  R.drawable.ic_local_acon_28,
            aconCount = localAcornCount.toString(),
            aconContentDescription = stringResource(com.acon.android.feature.spot.R.string.local_acon_content_description),
        )
        Spacer(modifier = Modifier.width(8.dp))

        AconIconAndCount(
            aconIcon =  R.drawable.ic_visitor_acon_28,
            aconCount = basicAcornCount.toString(),
            aconContentDescription = stringResource(com.acon.android.feature.spot.R.string.visitor_acon_content_description),
        )
        Spacer(modifier = Modifier.width(16.dp))

        AconFilledLargeButton(
            text = stringResource(com.acon.android.feature.spot.R.string.spot_detail_navigate_button),
            textStyle = AconTheme.typography.subtitle1_16_med,
            enabledBackgroundColor = AconTheme.color.Main_org1,
            disabledBackgroundColor = AconTheme.color.Main_org1,
            modifier = Modifier.fillMaxWidth(),
            onClick = onClickFindDirections,
            contentPadding = PaddingValues(vertical = 12.dp),
        )
    }
}

@Composable
fun AconIconAndCount(
    @DrawableRes aconIcon: Int,
    aconCount: String,
    aconContentDescription: String,
) {
    val textMeasurer = rememberTextMeasurer()

    val displayedCount = if (aconCount.length < 4) {
        val currentWidth = textMeasurer.measure(aconCount).size.width
        val desiredWidth = textMeasurer.measure(stringResource(com.acon.android.feature.spot.R.string.font_width_text)).size.width
        val spaceWidth = desiredWidth - currentWidth
        aconCount + " ".repeat((spaceWidth / textMeasurer.measure(" ").size.width))
    } else {
        if(aconCount.toInt() > 1000) { stringResource(com.acon.android.feature.spot.R.string.max_acon_count) }
        else { aconCount }
    }

    Row (
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = aconIcon),
            contentDescription = aconContentDescription
        )
        Text(
            text = displayedCount,
            style = AconTheme.typography.head8_16_sb,
            color = AconTheme.color.White,
            modifier = Modifier
                .padding(vertical = 4.dp),
        )
    }
}

@Preview
@Composable
private fun RestaurantBottomActionBarPreview() {
    AconTheme {
        RestaurantBottomActionBar(
            localAcornCount = 111,
            basicAcornCount = 123,
            onClickFindDirections = {}
        )
    }
}