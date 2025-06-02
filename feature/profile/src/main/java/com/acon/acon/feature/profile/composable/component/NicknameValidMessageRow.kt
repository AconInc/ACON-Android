package com.acon.acon.feature.profile.composable.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun NicknameValidMessageRow(
    @StringRes validMessage: Int,
    @DrawableRes iconRes: Int,
    @StringRes validContentDescription: Int,
    color: Color
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = stringResource(validContentDescription),
            tint = color
        )
        Text(
            text = stringResource(validMessage),
            style = AconTheme.typography.Body1,
            color = color,
            modifier = Modifier
                .padding(start = 4.dp)
        )
    }
}