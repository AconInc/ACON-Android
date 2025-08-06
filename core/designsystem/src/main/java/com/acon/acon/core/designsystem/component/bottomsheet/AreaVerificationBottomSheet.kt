package com.acon.acon.core.designsystem.component.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledTextButton
import com.acon.acon.core.designsystem.component.button.v2.AconOutlinedTextButton
import com.acon.acon.core.designsystem.theme.AconTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AreaVerificationBottomSheet(
    onDismissRequest: () -> Unit,
    onNavigateToAreaVerification: () -> Unit,
    modifier: Modifier = Modifier,
) {

    AconBottomSheet(onDismissRequest = onDismissRequest, modifier = modifier) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.request_area_verification_title),
                style = AconTheme.typography.Headline4,
                fontWeight = FontWeight.SemiBold,
                color = AconTheme.color.White,
                modifier = Modifier.padding(top = 24.dp)
            )
            Text(
                text = stringResource(R.string.request_area_verification_content),
                style = AconTheme.typography.Body1,
                fontWeight = FontWeight.W400,
                color = AconTheme.color.Gray200,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )

            Row(modifier = Modifier.padding(top = 86.dp)) {
                AconOutlinedTextButton(
                    text = stringResource(R.string.do_next),
                    textStyle = AconTheme.typography.Body1.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    onClick = onDismissRequest,
                    contentPadding = PaddingValues(vertical = 12.dp),
                    modifier = Modifier.weight(3f)
                )

                AconFilledTextButton(
                    text = stringResource(R.string.go_to_area_verification),
                    textStyle = AconTheme.typography.Body1.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    shape = CircleShape,
                    onClick = onNavigateToAreaVerification,
                    contentPadding = PaddingValues(vertical = 12.dp),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(5f)
                )
            }
        }
    }
}