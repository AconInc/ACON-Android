package com.acon.acon.feature.withdraw.screen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.blur.LocalHazeState
import com.acon.acon.core.designsystem.component.button.AconOutlinedLargeButton
import com.acon.acon.core.designsystem.component.radiobutton.AconRadioButton
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.keyboard.keyboardAsState
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.withdraw.component.DeleteAccountBottomSheet
import com.acon.acon.feature.withdraw.component.DeleteAccountTextField
import com.acon.acon.feature.withdraw.screen.DeleteAccountUiState
import com.acon.acon.feature.withdraw.type.DeleteReasonType
import com.acon.acon.feature.settings.R
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DeleteAccountScreen(
    state: DeleteAccountUiState,
    modifier: Modifier = Modifier,
    onUpdateReason: (String) -> Unit = {},
    navigateBack: () -> Unit = {},
    onDeleteAccount: () -> Unit = {},
    onBottomSheetShowStateChange: (Boolean) -> Unit = {}
) {
    var selectedReason by remember { mutableStateOf<DeleteReasonType?>(null) }
    var otherReasonText by remember { mutableStateOf("") }
    val submitButtonEnabled = selectedReason?.let {
        it != DeleteReasonType.OTHER || otherReasonText.isNotBlank()
    } ?: false

    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    val keyboardHeight by keyboardAsState()

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    when (state) {
        is DeleteAccountUiState.Default -> {
            if (state.showDeleteAccountBottomSheet) {
                DeleteAccountBottomSheet(
                    hazeState = LocalHazeState.current,
                    onDismissRequest = { onBottomSheetShowStateChange(false) },
                    onDeleteAccount = onDeleteAccount
                )
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(AconTheme.color.Gray9)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
                    .hazeSource(LocalHazeState.current)
            ) {
                Spacer(Modifier.height(42.dp))

                Column() {
                    AconTopBar(
                        modifier = Modifier.padding(vertical = 14.dp),
                        paddingValues = PaddingValues(0.dp),
                        leadingIcon = {
                            IconButton(onClick = navigateBack) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.ic_arrow_left_28),
                                    contentDescription = stringResource(R.string.go_back_content_description),
                                    tint = AconTheme.color.White
                                )
                            }
                        },
                        content = {
                            Text(
                                text = stringResource(R.string.settings_delete_account_topbar),
                                style = AconTheme.typography.head5_22_sb,
                                color = AconTheme.color.White
                            )
                        }
                    )

                    Text(
                        text = stringResource(R.string.settings_delete_account_title),
                        style = AconTheme.typography.head5_22_sb,
                        color = AconTheme.color.White
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.settings_delete_account_content),
                        style = AconTheme.typography.subtitle2_14_med,
                        color = AconTheme.color.Gray4
                    )

                    Spacer(Modifier.height(32.dp))

                    Column(
                        modifier = Modifier
                            .imePadding(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        DeleteReasonType.entries.forEach { reasonType ->
                            val reason = stringResource(reasonType.reason)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                AconRadioButton(
                                    selected = selectedReason == reasonType,
                                    onClick = {
                                        selectedReason = reasonType
                                        onUpdateReason(reason)
                                    }
                                )
                                Text(
                                    text = reason,
                                    style = AconTheme.typography.subtitle1_16_med,
                                    color = AconTheme.color.White,
                                    modifier = Modifier.padding(
                                        start = 8.dp,
                                        top = 1.dp,
                                        bottom = 1.dp
                                    )
                                )
                            }
                        }
                        if (selectedReason == DeleteReasonType.OTHER) {
                            DeleteAccountTextField(
                                value = otherReasonText,
                                onValueChange = { otherReasonText = it },
                                placeholder = stringResource(R.string.reason_other_placeholder),
                                focusRequester = focusRequester,
                                modifier = Modifier
                                    .bringIntoViewRequester(bringIntoViewRequester)
                                    .onFocusChanged { state ->
                                        isFocused = state.isFocused
                                        if (state.isFocused) {
                                            scope.launch {
                                                delay(300)
                                                scrollState.animateScrollTo(scrollState.maxValue)
                                                if (keyboardHeight > 0) {
                                                    bringIntoViewRequester.bringIntoView()
                                                }
                                            }
                                        } else { onUpdateReason(otherReasonText) }
                                    }
                            )
                        }
                        LaunchedEffect(keyboardHeight) {
                            if (keyboardHeight == 0) {
                                focusManager.clearFocus()
                                isFocused = false
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))
                AconOutlinedLargeButton(
                    text = stringResource(R.string.submit_btn_content),
                    enabledBorderColor = Color.Transparent,
                    enabledBackgroundColor = AconTheme.color.Gray5,
                    disabledBorderColor = Color.Transparent,
                    disabledBackgroundColor = AconTheme.color.Gray7,
                    onClick = {
                        if (submitButtonEnabled) {
                            onBottomSheetShowStateChange(true)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
                    textStyle = AconTheme.typography.head7_18_sb,
                    enabledTextColor = AconTheme.color.White,
                    disabledTextColor = AconTheme.color.Gray5,
                    isEnabled = submitButtonEnabled,
                    contentPadding = PaddingValues(vertical = 13.dp)
                )
            }
        }
    }

}


@Preview
@Composable
fun DeleteAccountScreenPreview() {
    AconTheme {
        DeleteAccountScreen(
            state = DeleteAccountUiState.Default()
        )
    }
}

