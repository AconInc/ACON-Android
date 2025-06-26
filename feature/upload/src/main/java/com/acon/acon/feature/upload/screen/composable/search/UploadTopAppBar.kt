package com.acon.acon.feature.upload.screen.composable.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
internal fun UploadTopAppBar(
    isRightActionEnabled: Boolean,
    onLeftAction: () -> Unit,
    onRightAction: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_x_mark),
            contentDescription = stringResource(R.string.close_upload),
            tint = AconTheme.color.Gray50,
            modifier = Modifier
                .noRippleClickable { onLeftAction() }
        )
        Text(
            text = stringResource(R.string.title_upload),
            style = AconTheme.typography.Title4,
            fontWeight = FontWeight.SemiBold,
            color = AconTheme.color.White,
        )
        Text(
            text = stringResource(R.string.next),
            style = AconTheme.typography.Title4,
            fontWeight = FontWeight.SemiBold,
            color = if(isRightActionEnabled) AconTheme.color.Action else AconTheme.color.Gray300,
            modifier = Modifier
                .noRippleClickable(enabled = isRightActionEnabled) {
                    if (isRightActionEnabled) {
                        onRightAction()
                    }
                }
        )
    }
}