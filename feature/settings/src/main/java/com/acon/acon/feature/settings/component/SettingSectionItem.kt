package com.acon.acon.feature.settings.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.settings.type.SettingsType
import com.acon.acon.feature.settings.R

@Composable
fun SettingSectionItem(
    settingsType: SettingsType,
    modifier: Modifier = Modifier,
    onClickContinue: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = AconTheme.color.Gray9)
            .noRippleClickable { onClickContinue() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = AconTheme.color.Gray8,
                    shape = RoundedCornerShape(13.dp)
                )
                .padding(8.dp)
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = settingsType.titleResId),
                contentDescription = stringResource(settingsType.title),
                modifier = Modifier
                    .size(17.dp)
            )
        }

        Text(
            text = stringResource(id = settingsType.title),
            style = AconTheme.typography.subtitle1_16_med,
            color = AconTheme.color.White,
            modifier = Modifier
                .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                .weight(1f)
        )

        Icon(
            imageVector = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.ic_arrow_right_20),
            contentDescription = stringResource(R.string.execute_settings_content_description),
            modifier = Modifier
                .padding(vertical = 6.dp),
            tint = AconTheme.color.Gray4
        )
    }

}

@Preview()
@Composable
private fun SettingSectionItemPreview() {
    AconTheme {
        SettingSectionItem(
            settingsType = SettingsType.LOGOUT,
            onClickContinue = {}
        )
    }
}