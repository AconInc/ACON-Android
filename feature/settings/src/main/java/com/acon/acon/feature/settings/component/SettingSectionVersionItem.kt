package com.acon.acon.feature.settings.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
fun SettingSectionVersionItem(
    currentVersion: String,
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
                imageVector = ImageVector.vectorResource(id = SettingsType.CURRENT_VERSION.titleResId),
                contentDescription = stringResource(R.string.current_version_info_content_description),
                modifier = Modifier
                    .size(17.dp)
            )
        }
        Text(
            text = stringResource(R.string.settings_section_current_version),
            style = AconTheme.typography.subtitle1_16_med,
            color = AconTheme.color.White,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
        )

        // TODO - 앱 출시 후 버전 분기 처리
        if(currentVersion.isNotBlank()) {
            Text(
                text = currentVersion,
                style = AconTheme.typography.subtitle2_14_med,
                color = AconTheme.color.Gray4,
                modifier = Modifier
                    .padding(vertical = 6.dp),
            )
        } else {
            Row(
                modifier = Modifier
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentVersion,
                    style = AconTheme.typography.subtitle2_14_med,
                    color = AconTheme.color.Gray4,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.ic_arrow_right_20),
                    contentDescription = stringResource(R.string.execute_settings_content_description),
                    tint = AconTheme.color.Gray4
                )
            }
        }
    }

}

@Preview()
@Composable
private fun SettingSectionVersionItemPreview() {
    AconTheme {
        SettingSectionVersionItem(
            currentVersion = "최신버전",
        )
    }
}