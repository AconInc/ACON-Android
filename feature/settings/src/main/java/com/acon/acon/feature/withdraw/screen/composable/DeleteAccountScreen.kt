package com.acon.acon.feature.withdraw.screen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.component.radiobutton.AconRadioButton
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.effect.imageGradientLayer
import com.acon.acon.core.designsystem.keyboard.keyboardAsState
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.withdraw.amplitude.deleteAccountAmplitudeExitReason
import com.acon.acon.feature.withdraw.amplitude.deleteAccountAmplitudeSubmit
import com.acon.acon.feature.withdraw.amplitude.deleteAccountAmplitudeWithDraw
import com.acon.acon.feature.withdraw.component.DeleteAccountBottomSheet
import com.acon.acon.feature.withdraw.component.DeleteAccountTextField
import com.acon.acon.feature.withdraw.screen.DeleteAccountUiState
import com.acon.acon.feature.withdraw.type.DeleteReasonType
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
                    onDismissRequest = { onBottomSheetShowStateChange(false) },
                    onDeleteAccount = {
                        onDeleteAccount()
                        deleteAccountAmplitudeWithDraw()
                    }
                )
            }

            Box(
                modifier = Modifier
                    .background(AconTheme.color.Gray900)
                    .statusBarsPadding()
                    .fillMaxSize()
            ) {
                AconTopBar(
                    leadingIcon = {
                        IconButton(
                            onClick = { navigateBack() }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_topbar_arrow_left),
                                contentDescription = stringResource(R.string.back),
                                tint = AconTheme.color.Gray50
                            )
                        }
                    },
                    content = {
                        Text(
                            text = stringResource(R.string.settings_delete_account_topbar),
                            style = AconTheme.typography.Title4,
                            fontWeight = FontWeight.SemiBold,
                            color = AconTheme.color.White
                        )
                    },
                    modifier = Modifier
                        .padding(vertical = 14.dp)
                        .imageGradientLayer()
                        .defaultHazeEffect(
                            hazeState = LocalHazeState.current,
                            tintColor = AconTheme.color.Gray900,
                            blurRadius = 20.dp,
                        )
                        .zIndex(1f)
                )

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(AconTheme.color.Gray900)
                        .padding(top = 56.dp)
                        .padding(horizontal = 16.dp)
                        .navigationBarsPadding()
                        .verticalScroll(scrollState)
                        .hazeSource(LocalHazeState.current)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    focusManager.clearFocus()
                                }
                            )
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 40.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.settings_delete_account_title),
                            style = AconTheme.typography.Headline4,
                            fontWeight = FontWeight.SemiBold,
                            color = AconTheme.color.White,
                        )

                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.settings_delete_account_content),
                            style = AconTheme.typography.Body1,
                            color = AconTheme.color.Gray500
                        )

                        Spacer(Modifier.height(40.dp))
                        Column(
                            modifier = Modifier
                                .imePadding(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            DeleteReasonType.entries.forEach { reasonType ->
                                val reason = stringResource(reasonType.reason)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .noRippleClickable {
                                            selectedReason = reasonType
                                            onUpdateReason(reason)
                                        },
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    AconRadioButton(
                                        selected = selectedReason == reasonType
                                    )
                                    Text(
                                        text = reason,
                                        style = AconTheme.typography.Title5,
                                        color = AconTheme.color.White,
                                        modifier = Modifier.padding(
                                            start = 8.dp
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
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus()
                                            onUpdateReason(otherReasonText)
                                        }
                                    ),
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
                                            } else {
                                                onUpdateReason(otherReasonText)
                                            }
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
                    AconFilledButton(
                        onClick = {
                            if (submitButtonEnabled) {
                                onBottomSheetShowStateChange(true)
                                deleteAccountAmplitudeSubmit()
                                deleteAccountAmplitudeExitReason(state.reason)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        enabled = submitButtonEnabled
                    ) {
                        Text(
                            text = stringResource(R.string.submit),
                            style = AconTheme.typography.Title4,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
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