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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.verification.component.VerifiedAreaChip
import com.acon.acon.feature.verification.screen.LocalVerificationUiState

@Composable
fun LocalVerificationScreen(
    state: LocalVerificationUiState,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onclickAddArea: () -> Unit = {},
    onclickEditArea: (String) -> Unit = {},
    onDeleteVerifiedAreaChip: (Long) -> Unit = {},
) {

    when(state) {
        is LocalVerificationUiState.Success -> {
            Column(
                modifier = modifier
                    .background(AconTheme.color.Gray900)
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
                                    R.drawable.ic_topbar_arrow_left
                                ),
                                contentDescription = stringResource(R.string.back),
                                tint = AconTheme.color.White
                            )
                        }
                    },
                    content = {
                        Text(
                            text = stringResource(R.string.settings_area_verification_topbar),
                            color = AconTheme.color.White,
                            fontWeight = FontWeight.SemiBold,
                            style = AconTheme.typography.Title4
                        )
                    }
                )

                Spacer(Modifier.height(40.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(R.string.settings_area_verification_title),
                        style = AconTheme.typography.Title4,
                        fontWeight = FontWeight.SemiBold,
                        color = AconTheme.color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.star),
                        style = AconTheme.typography.Title4,
                        fontWeight = FontWeight.SemiBold,
                        color = AconTheme.color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.settings_area_verification_content),
                    style = AconTheme.typography.Body1,
                    color = AconTheme.color.Gray500
                )

                Spacer(Modifier.height(16.dp))
                VerifiedAreaChip(
                    areaList = state.verificationAreaList,
                    onEditArea = {
                        if(state.verificationAreaList.size == 1) {
                            onclickEditArea(state.verificationAreaList[0].name)
                        }
                    },
                    onRemoveChip = {
                        state.selectedAreaId?.let { verifiedAreaId ->
                            onDeleteVerifiedAreaChip(verifiedAreaId)
                        }
                    },
                    onClickAddArea = onclickAddArea
                )
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