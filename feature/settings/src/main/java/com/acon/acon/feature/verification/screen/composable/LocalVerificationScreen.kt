package com.acon.acon.feature.verification.screen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.component.dialog.AconTwoButtonDialog
import com.acon.acon.core.designsystem.component.snackbar.AconTextSnackBar
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.settings.R
import com.acon.acon.feature.verification.component.AddAreaButton
import com.acon.acon.feature.verification.component.VerifiedAreaChip
import com.acon.acon.feature.verification.screen.LocalVerificationUiState
import kotlinx.coroutines.launch

@Composable
fun LocalVerificationScreen(
    state: LocalVerificationUiState,
    modifier: Modifier = Modifier,
    maxLocalVerificationArea: Int = 5,
    onNavigateBack: () -> Unit = {},
    onclickAddArea: () -> Unit = {},
    onclickEditArea: (String) -> Unit = {},
    onDeleteVerifiedAreaChip: (Long) -> Unit = {},
    onShowEditVerifiedAreaChipDialog: (Boolean) -> Unit = {},
    onShowDeleteVerifiedAreaChipDialog: (Boolean, Long) -> Unit = { _, _ -> }
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    when(state) {
        is LocalVerificationUiState.Success -> {
            if(state.showEditVerifiedAreaChipDialog) {
                AconTwoButtonDialog(
                    title = stringResource(R.string.area_edit_dialog_title),
                    content = stringResource(R.string.area_edit_dialog_content),
                    leftButtonContent = stringResource(R.string.area_edit_dialog_left_btn),
                    rightButtonContent = stringResource(R.string.area_edit_dialog_right_btn),
                    onDismissRequest = { onShowEditVerifiedAreaChipDialog(false) },
                    onClickLeft = { onShowEditVerifiedAreaChipDialog(false) } ,
                    onClickRight = {
                        onShowEditVerifiedAreaChipDialog(false)
                        if(state.verificationAreaList.size == 1) {
                            onclickEditArea(state.verificationAreaList[0].name)
                        }
                    },
                )
            }

            if(state.showDeleteVerifiedAreaChipDialog) {
                val selectedAreaName = state.verificationAreaList
                    .find { it.verifiedAreaId == state.selectedAreaId }?.name ?: ""
                val dialogTitle = stringResource(id = R.string.area_delete_dialog_title, selectedAreaName)
                val snackbarTitle = stringResource(id = R.string.area_delete_snackbar_title, selectedAreaName)

                AconTwoButtonDialog(
                    title = dialogTitle,
                    leftButtonContent = stringResource(R.string.logout_dialog_cancel_btn),
                    rightButtonContent = stringResource(R.string.area_delete_dialog_right_btn),
                    onDismissRequest = { onShowDeleteVerifiedAreaChipDialog(false, -1) },
                    onClickLeft = { onShowDeleteVerifiedAreaChipDialog(false, -1) } ,
                    onClickRight = {
                        state.selectedAreaId?.let { verifiedAreaId ->
                            onDeleteVerifiedAreaChip(verifiedAreaId)
                        }
                        onShowDeleteVerifiedAreaChipDialog(false, -1)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(snackbarTitle)
                        }
                    },
                )
            }

            Column(
                modifier = modifier
                    .background(AconTheme.color.Gray9)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(42.dp))
                AconTopBar(
                    modifier = Modifier
                        .padding(vertical = 14.dp),
                    paddingValues = PaddingValues(0.dp),
                    leadingIcon = {
                        IconButton(
                            onClick = onNavigateBack
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    com.acon.acon.core.designsystem.R.drawable.ic_arrow_left_28
                                ),
                                contentDescription = stringResource(R.string.go_back_content_description),
                                tint = AconTheme.color.White
                            )
                        }
                    },
                    content = {
                        Text(
                            text = stringResource(R.string.local_verification_topbar),
                            style = AconTheme.typography.head5_22_sb,
                            color = AconTheme.color.White
                        )
                    }
                )

                Spacer(Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(R.string.local_verification_title),
                        style = AconTheme.typography.head8_16_sb,
                        color = AconTheme.color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.local_verification_title_star),
                        style = AconTheme.typography.head8_16_sb,
                        color = AconTheme.color.Main_org1
                    )
                }

                Spacer(Modifier.height(12.dp))
                AddAreaButton(
                    text = stringResource(R.string.local_verification_add_area),
                    contentImage = com.acon.acon.core.designsystem.R.drawable.ic_add_16,
                    enabledBorderColor = AconTheme.color.Gray5,
                    enabledBackgroundColor = AconTheme.color.Gray9,
                    enabledTextColor = AconTheme.color.White,
                    disabledBorderColor = AconTheme.color.Gray6,
                    disabledBackgroundColor = AconTheme.color.Gray7,
                    disabledTextColor = AconTheme.color.Gray5,
                    onClick = {
                        onclickAddArea()
                    },
                    textStyle = AconTheme.typography.subtitle1_16_med,
                    isEnabled = state.verificationAreaList.size < maxLocalVerificationArea,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))
                VerifiedAreaChip(
                    areaList = state.verificationAreaList,
                    onEditArea = { showDialog ->
                        onShowEditVerifiedAreaChipDialog(showDialog)
                    },
                    onRemoveChip = { showDialog, areaId ->
                        onShowDeleteVerifiedAreaChipDialog(showDialog, areaId)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.weight(1f))
                SnackbarHost(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    hostState = snackbarHostState
                ) { snackbarData: SnackbarData ->
                    AconTextSnackBar(
                        message = snackbarData.visuals.message
                    )
                }
                Spacer(Modifier.height(112.dp))
            }
        }

        is LocalVerificationUiState.Loading -> {}
        is LocalVerificationUiState.LoadFailed -> {}
    }
}

@Preview
@Composable
fun LocalVerificationScreenPreview() {
    AconTheme {
        LocalVerificationScreen(
            state = LocalVerificationUiState.Success(
                verificationAreaList = emptyList()
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun VerifiedAreaEditDialogPreview() {
    AconTheme {
        AconTwoButtonDialog(
            title = stringResource(R.string.area_edit_dialog_title),
            content = stringResource(R.string.area_edit_dialog_content),
            leftButtonContent = stringResource(R.string.area_edit_dialog_left_btn),
            rightButtonContent = stringResource(R.string.area_edit_dialog_right_btn),
            onDismissRequest = {},
            onClickLeft = {},
            onClickRight = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VerifiedAreaDeleteDialogPreview() {
    val dialogTitle = stringResource(id = R.string.area_delete_dialog_title, "망원동")

    AconTheme {
        AconTwoButtonDialog(
            title = dialogTitle,
            leftButtonContent = stringResource(R.string.logout_dialog_cancel_btn),
            rightButtonContent = stringResource(R.string.area_delete_dialog_right_btn),
            onDismissRequest = {},
            onClickLeft = {},
            onClickRight = {}
        )
    }
}