package com.acon.acon.feature.spot.screen.spotdetail.composable.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun MoveToTopFAB(
    onClickFab: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .noRippleClickable { onClickFab() }
    ) {
        Image(
            imageVector = ImageVector.vectorResource(
                com.acon.acon.core.designsystem.R.drawable.ic_arrow_up_30
            ),
            contentDescription = stringResource(com.acon.acon.feature.spot.R.string.top_button_description),
            modifier = Modifier
                .padding(all = 9.dp),
        )
    }
}

@Preview
@Composable
private fun MoveToTopFABPreview() {
    AconTheme {
        MoveToTopFAB(
            onClickFab = {}
        )
    }
}
