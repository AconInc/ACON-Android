package com.acon.acon.feature.spot.screen.spotlist.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.acon.acon.feature.spot.R

@Composable
fun EmptySpotListView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            imageVector = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.ic_error_1_120),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.alert_no_spot),
            style = AconTheme.typography.body2_14_reg,
            color = AconTheme.color.Gray4,
            modifier = Modifier.padding(top = 24.dp)
        )
        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Preview
@Composable
private fun EmptySpotListViewPreview() {
    EmptySpotListView(
        modifier = Modifier.fillMaxSize()
    )
}