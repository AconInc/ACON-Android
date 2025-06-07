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
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun SignInTopBar(
    onClickText: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(AconTheme.color.Gray900)
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .padding(end = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = stringResource(R.string.signin_topbar_text),
            style = AconTheme.typography.Body1,
            color = AconTheme.color.White,
            modifier = Modifier
                .padding(8.dp)
                .noRippleClickable { onClickText() }
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SignInTopBarPreview() {
    AconTheme {
        SignInTopBar(
            onClickText = {}
        )
    }
}