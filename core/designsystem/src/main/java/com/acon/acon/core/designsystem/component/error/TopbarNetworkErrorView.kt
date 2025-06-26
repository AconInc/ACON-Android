package com.acon.acon.core.designsystem.component.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun TopbarNetworkErrorView(
    onRetry: () -> Unit,
    onNavBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        AconTopBar(
            leadingIcon = {
                IconButton(
                    onClick = { onNavBack() }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_topbar_arrow_left),
                        contentDescription = stringResource(R.string.back),
                        tint = AconTheme.color.Gray50
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 44.dp, bottom = 14.dp)
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_caution),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = AconTheme.color.Gray50
            )
            Text(
                text = stringResource(R.string.network_error_title),
                style = AconTheme.typography.Title3,
                color = AconTheme.color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 20.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.network_error_content),
                style = AconTheme.typography.Body1,
                color = AconTheme.color.White,
                fontWeight = FontWeight.W400,
                modifier = Modifier.padding(top = 12.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.network_retry),
                style = AconTheme.typography.Body1,
                color = AconTheme.color.Action,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(top = 48.dp)
                    .noRippleClickable {
                        onRetry()
                    }
                    .padding(8.dp)
            )
        }
    }
}

@Composable
@Preview
private fun TopbarNetworkErrorViewPreview() {
    NetworkErrorView(onRetry = {})
}