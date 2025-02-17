package com.acon.feature.profile.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun NicknameErrMessageRow(
    modifier: Modifier = Modifier,
    errMessage: String = "",
    iconRes: ImageVector,
    textColor: Color
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        Icon(
            imageVector = iconRes,
            contentDescription = "ErrorMessage Icon",
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .size(16.dp)
        )
        Text(
            text = errMessage,
            style = AconTheme.typography.subtitle2_14_med,
            color = textColor
        )
    }
}