package com.acon.acon.feature.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun SettingSectionVersionItem(
    isLatestVersion: Boolean,
    modifier: Modifier = Modifier,
    onClickContinue: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = AconTheme.color.Gray900),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.settings_section_current_version),
            style = AconTheme.typography.Title4,
            color = AconTheme.color.White,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        )

        // TODO - 버전 분기 처리
        if (isLatestVersion) {
            Text(
                text = stringResource(R.string.latest_version),
                style = AconTheme.typography.Body1,
                color = AconTheme.color.Gray500,
                modifier = Modifier
                    .padding(vertical = 2.dp)
            )
        } else {
            Text(
                text = stringResource(R.string.do_update),
                style = AconTheme.typography.Body1,
                color = AconTheme.color.Action,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .noRippleClickable { onClickContinue() }
            )
        }
    }

}

@Preview()
@Composable
private fun SettingSectionVersionItemPreview() {
    AconTheme {
        SettingSectionVersionItem(
            isLatestVersion = false
        )
    }
}