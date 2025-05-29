package com.acon.acon.feature.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.settings.type.SettingsType

@Composable
fun SettingSectionItem(
    settingsType: SettingsType,
    modifier: Modifier = Modifier,
    onClickContinue: () -> Unit = {},
) {
    val textColor =
        if (settingsType == SettingsType.DELETE_ACCOUNT) AconTheme.color.Danger
        else AconTheme.color.White

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = AconTheme.color.Gray900)
            .noRippleClickable { onClickContinue() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = settingsType.title),
            style = AconTheme.typography.Title4,
            color = textColor,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_settings_arrow_forward),
            contentDescription = stringResource(settingsType.title),
            modifier = Modifier
                .padding(vertical = 3.dp),
            tint = AconTheme.color.Gray50
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