package com.acon.acon.core.designsystem.component.bottomsheet

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.common.UrlConstants
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.AconGoogleSignInButton
import com.acon.acon.core.designsystem.theme.AconTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInBottomSheet(
    onDismissRequest: () -> Unit,
    onGoogleSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    AconBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 54.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.sign_in_bottom_sheet_title),
                style = AconTheme.typography.Headline4,
                color = AconTheme.color.White,
                modifier = Modifier.padding(top = 24.dp)
            )

            Text(
                text = stringResource(R.string.sign_in_bottom_sheet_content),
                style = AconTheme.typography.Body1,
                color = AconTheme.color.Gray2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(Modifier.height(190.dp))
            AconGoogleSignInButton(
                onClick = onGoogleSignIn
            )

            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.sign_in_bottom_sheet_policy_agreement),
                style = AconTheme.typography.body2_14_reg,
                color = AconTheme.color.Gray3,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.sign_in_bottom_sheet_term_of_use),
                    style = AconTheme.typography.Caption1,
                    color = AconTheme.color.White,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(UrlConstants.TERM_OF_USE))
                        context.startActivity(intent)
                    }
                )

                Text(
                    text = stringResource(R.string.sign_in_bottom_sheet_private_policy),
                    style = AconTheme.typography.Caption1,
                    color = AconTheme.color.White,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(UrlConstants.PRIVATE_POLICY))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInBottomSheetPreview() {
    AconTheme {
        SignInBottomSheet(
            onDismissRequest = {},
            onGoogleSignIn = {}
        )
    }
}