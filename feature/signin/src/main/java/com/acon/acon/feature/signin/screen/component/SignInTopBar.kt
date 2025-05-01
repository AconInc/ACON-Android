package com.acon.acon.feature.signin.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun SignInTopBar(
    modifier: Modifier = Modifier,
    onClickText: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .background(AconTheme.color.Gray9)
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = stringResource(com.acon.acon.feature.signin.R.string.signin_topbar_text),
            style = AconTheme.typography.subtitle2_14_med,
            color = AconTheme.color.White,
            modifier = Modifier
                .padding(end = 20.dp)
                .noRippleClickable { onClickText() }
        )
    }
}


@Composable
@Preview(showBackground = true)
private fun SignInTopBarPreview() {
    AconTheme {
        SignInTopBar()
    }
}