package com.acon.acon.feature.withdraw.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.bottomsheet.AconBottomSheet
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.theme.AconTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountBottomSheet(
    onDeleteAccount: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AconBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(34.dp))
            Text(
                text = stringResource(R.string.delete_account_bottom_sheet_title),
                style = AconTheme.typography.Headline4,
                color = AconTheme.color.White
            )

            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.delete_account_bottom_sheet_content),
                style = AconTheme.typography.Body1,
                color = AconTheme.color.Gray300,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 86.dp, bottom = 24.dp)
            ) {
                AconFilledButton(
                    modifier = Modifier.weight(3f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AconTheme.color.GlassWhiteDefault,
                        contentColor = AconTheme.color.White
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = AconTheme.color.GlassWhiteDefault
                    ),
                    onClick = onDismissRequest,
                    shape = RoundedCornerShape(100.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = AconTheme.typography.Body1,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(Modifier.width(8.dp))
                AconFilledButton(
                    modifier = Modifier.weight(5f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AconTheme.color.GlassWhiteDefault,
                        contentColor = AconTheme.color.White
                    ),
                    onClick = onDeleteAccount,
                    shape = RoundedCornerShape(100.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.delete_account_btn_content),
                        style = AconTheme.typography.Body1,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteAccountBottomSheetPreview() {
    AconTheme {
        DeleteAccountBottomSheet(
            onDismissRequest = {},
            onDeleteAccount = {}
        )
    }
}