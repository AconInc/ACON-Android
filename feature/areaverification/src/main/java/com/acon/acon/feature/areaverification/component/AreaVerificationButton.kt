package com.acon.acon.feature.areaverification.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.component.button.AconButton
import com.acon.acon.core.designsystem.component.radiobutton.AconRadioButton
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.areaverification.R

@Composable
fun AreaVerificationButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    AconButton(
        backGroundColor = if (selected) AconTheme.color.Gray8 else AconTheme.color.Gray9,
        borderColor = if (selected) AconTheme.color.Gray7 else AconTheme.color.Gray8,
        borderWidth = 1.dp,
        modifier = modifier,
        cornerRadius = 6.dp,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        onClick = onClick,
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            AconRadioButton(
                selected = selected,
                onClick = onClick,
                enabled = isEnabled
            )
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.area_verification_new),
                    style = AconTheme.typography.subtitle1_16_med,
                    color = AconTheme.color.Main_org1
                )
                Text(
                    text = " ",
                    style = AconTheme.typography.head5_22_sb,
                    color = AconTheme.color.Gray1
                )
                Text(
                    text = stringResource(R.string.area_verification_certify_my_area),
                    style = AconTheme.typography.subtitle1_16_med,
                    color = AconTheme.color.White
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAreaVerificationButton() {
    AconTheme {
        AreaVerificationButton(
            selected = true,
            onClick = {},
            isEnabled = true
        )
    }
}

@Preview
@Composable
private fun PreviewUnselectedAreaVerificationButton() {
    AconTheme {
        AreaVerificationButton(
            selected = false,
            onClick = {},
            isEnabled = true
        )
    }
}

@Preview
@Composable
private fun PreviewDisabledAreaVerificationButton() {
    AconTheme {
        AreaVerificationButton(
            selected = false,
            onClick = {},
            isEnabled = false
        )
    }
}
