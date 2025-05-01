package com.acon.acon.feature.withdraw.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.blur.defaultHazeEffect
import com.acon.acon.core.designsystem.component.button.AconFilledLargeButton
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.settings.R
import dev.chrisbanes.haze.HazeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountBottomSheet(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onDeleteAccount: () -> Unit = {},
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 32.dp, bottom = 40.dp)
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(
                            id = com.acon.acon.core.designsystem.R.drawable.ic_error_1_120
                        ),
                        contentDescription = stringResource(R.string.delete_account_service_content_description),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(32.dp))
                    Text(
                        text = stringResource(R.string.delete_account_bottom_sheet_title),
                        style = AconTheme.typography.head5_22_sb,
                        color = AconTheme.color.White
                    )

                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.delete_account_bottom_sheet_content),
                        style = AconTheme.typography.subtitle1_16_med,
                        color = AconTheme.color.Gray3
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 42.dp)
                    ) {
                        AconFilledLargeButton(
                            text = stringResource(R.string.cancel_btn_content),
                            textStyle = AconTheme.typography.head7_18_sb,
                            enabledBackgroundColor = AconTheme.color.Gray5,
                            disabledBackgroundColor = AconTheme.color.Gray5,
                            onClick = onDismissRequest,
                            contentPadding = PaddingValues(vertical = 13.dp, horizontal = 20.dp)
                        )

                        Spacer(Modifier.width(8.dp))
                        AconFilledLargeButton(
                            text = stringResource(R.string.delete_account_btn_content),
                            textStyle = AconTheme.typography.head7_18_sb,
                            enabledBackgroundColor = AconTheme.color.Main_org1,
                            disabledBackgroundColor = AconTheme.color.Main_org1,
                            onClick = onDeleteAccount,
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(vertical = 13.dp)
                        )
                    }
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
            hazeState = HazeState(),
            onDismissRequest = {},
            onDeleteAccount = {}
        )
    }
}


