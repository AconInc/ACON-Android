package com.acon.feature.onboarding.area.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.component.button.v2.AconFilledButton
import com.acon.acon.core.designsystem.component.popup.AconToastPopup
import com.acon.acon.core.designsystem.component.topbar.AconTopBar
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.core.map.composable.NaverMapView
import com.acon.feature.onboarding.area.viewmodel.VerifyInMapState

@Composable
internal fun VerifyInMapScreen(
    state: VerifyInMapState,
    onCompleteButtonClick: () -> Unit,
    onBackIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
    ) {
        AconTopBar(
            leadingIcon = {
                IconButton(
                    onClick = onBackIconClick
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_topbar_arrow_left),
                        contentDescription = stringResource(R.string.back),
                        tint = AconTheme.color.Gray50
                    )
                }
            },
            content = {
                Text(
                    text = stringResource(R.string.area_verification_topbar),
                    style = AconTheme.typography.Title4,
                    color = AconTheme.color.White
                )
            },
            modifier = Modifier
                .background(
                    color = AconTheme.color.DimDefault.copy(alpha = 0.8f)
                )
                .padding(vertical = 14.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {

            AconToastPopup(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 17.dp)
                    .padding(horizontal = 16.dp),
                color = AconTheme.color.DimDefault.copy(
                    alpha = 0.8f
                ),
                content = {
                    Text(
                        text = stringResource(R.string.area_verification_popup),
                        color = AconTheme.color.White,
                        style = AconTheme.typography.Body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 13.dp)
                    )
                }
            )

            if (state.latitude != null && state.longitude != null)
                NaverMapView(
                    latitude = state.latitude,
                    longitude = state.longitude,
                    modifier = Modifier.fillMaxSize(),
                )

            AconFilledButton(
                onClick = onCompleteButtonClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AconTheme.color.DimDefault.copy(
                        alpha = 0.8f
                    ),
                    contentColor = AconTheme.color.White
                ),
                content = {
                    Text(
                        text = stringResource(R.string.area_verification_btn_content),
                        style = AconTheme.typography.Title4,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        }
    }
}