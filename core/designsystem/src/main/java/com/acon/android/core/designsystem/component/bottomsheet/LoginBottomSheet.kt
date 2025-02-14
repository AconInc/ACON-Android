package com.acon.android.core.designsystem.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.android.core.designsystem.R
import com.acon.android.core.designsystem.blur.defaultHazeEffect
import com.acon.android.core.designsystem.component.button.AconGoogleLoginButton
import com.acon.android.core.designsystem.noRippleClickable
import com.acon.android.core.designsystem.theme.AconTheme
import dev.chrisbanes.haze.HazeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginBottomSheet(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    onDismissRequest:() -> Unit = {},
    onGoogleSignIn: () -> Unit = {},
    onTermOfUse: () -> Unit = {},
    onPrivatePolicy: () -> Unit = {},
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        contentColor = AconTheme.color.Gray9.copy(alpha = 0.5f),
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        dragHandle = null
    ) {
        Box {
           Column (
               modifier = modifier
                   .fillMaxWidth()
                   .fillMaxHeight(0.6f)
                   .background(AconTheme.color.Gray9.copy(alpha = 0.5f))
                   .defaultHazeEffect(
                       hazeState = hazeState,
                       tintColor = AconTheme.color.Gray8,
                       alpha = 0.7f,
                       blurRadius = 20.dp
                   )
           ) {
               Spacer(
                   modifier = Modifier
                       .align(Alignment.CenterHorizontally)
                       .padding(vertical = 8.dp)
                       .clip(CircleShape)
                       .size(width = 36.dp, height = 5.dp)
                       .background(AconTheme.color.Gray5)
               )
               Box(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(top = 12.dp),
                   contentAlignment = Alignment.Center
               ) {
                   Icon(
                       imageVector = ImageVector.vectorResource(R.drawable.ic_dissmiss_28),
                       contentDescription = stringResource(R.string.content_description_dismiss_bottom_sheet),
                       modifier = Modifier
                           .align(Alignment.CenterEnd)
                           .padding(end = 24.dp)
                           .noRippleClickable {
                               onDismissRequest()
                           },
                       tint = AconTheme.color.White
                   )
               }
               Column(
                   modifier = Modifier
                       .padding(horizontal = 16.dp)
                       .padding(top = 32.dp, bottom = 80.dp),
                   horizontalAlignment = Alignment.CenterHorizontally
               ) {
                   Text(
                       text = stringResource(R.string.login_bottom_sheet_title),
                       style = AconTheme.typography.head5_22_sb,
                       color = AconTheme.color.White
                   )

                   Spacer(Modifier.height(8.dp))
                   Text(
                       text = stringResource(R.string.login_bottom_sheet_content),
                       style = AconTheme.typography.body1_15_reg,
                       color = AconTheme.color.Gray2,
                       textAlign = TextAlign.Center,
                       modifier = Modifier.weight(1f)
                   )

                   AconGoogleLoginButton(
                       onClick = onGoogleSignIn,
                       textStyle = AconTheme.typography.subtitle1_16_med,
                   )

                   Spacer(Modifier.height(16.dp))
                   Text(
                       text = stringResource(R.string.login_bottom_sheet_policy_agreement),
                       style = AconTheme.typography.body2_14_reg,
                       color = AconTheme.color.Gray3,
                       textAlign = TextAlign.Center,
                   )

                   Spacer(Modifier.height(4.dp))
                   Row(
                       horizontalArrangement = Arrangement.spacedBy(16.dp)
                   ) {
                       Text(
                           text = stringResource(R.string.login_bottom_sheet_term_of_use),
                           style = AconTheme.typography.body2_14_reg,
                           color = AconTheme.color.Gray5,
                           textDecoration = TextDecoration.Underline,
                           modifier = Modifier.clickable { onTermOfUse() }
                       )

                       Text(
                           text = stringResource(R.string.login_bottom_sheet_private_policy),
                           style = AconTheme.typography.body2_14_reg,
                           color = AconTheme.color.Gray5,
                           textDecoration = TextDecoration.Underline,
                           modifier = Modifier.clickable { onPrivatePolicy() }
                       )
                   }
               }
           }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginBottomSheetPreview() {
    AconTheme {
        LoginBottomSheet(
            hazeState = HazeState(),
            onDismissRequest = {},
            onGoogleSignIn = {},
            onTermOfUse = {},
            onPrivatePolicy = {}
        )
    }
}
