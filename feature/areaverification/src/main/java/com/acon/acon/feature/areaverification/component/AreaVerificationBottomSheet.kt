package com.acon.acon.feature.areaverification.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.component.button.AconFilledLargeButton
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.areaverification.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AreaVerificationBottomSheet(
    areaName: String,
    onDismiss: () -> Unit,
    onNavigateToNext: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.75f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(AconTheme.color.Gray9)
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .align(Alignment.CenterHorizontally)
                    .size(width = 36.dp, height = 5.dp)
                    .background(
                        color = AconTheme.color.Gray5,
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            Spacer(modifier = Modifier.padding(top = 78.dp))
            Text(
                text = stringResource(R.string.local_dottori_available, areaName),
                style = AconTheme.typography.head5_22_sb,
                color = AconTheme.color.White,
                modifier = Modifier.padding(start = 16.dp)
            )

            Text(
                text = stringResource(R.string.local_dottori_description),
                style = AconTheme.typography.body2_14_reg,
                color = AconTheme.color.Gray3,
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp)
            )

            Spacer(modifier = Modifier.padding(top = 54.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = com.acon.acon.core.designsystem.R.drawable.ic_local_acon),
                        contentDescription = stringResource(R.string.local_dottori),
                        modifier = Modifier.size(120.dp)
                    )
                    Text(
                        text = stringResource(R.string.local_dottori),
                        style = AconTheme.typography.subtitle1_16_med,
                        color = AconTheme.color.Gray1
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = com.acon.acon.core.designsystem.R.drawable.ic_normal_acon),
                        contentDescription = stringResource(R.string.normal_dottori),
                        modifier = Modifier.size(120.dp)
                    )
                    Text(
                        text = stringResource(R.string.normal_dottori),
                        style = AconTheme.typography.subtitle1_16_med,
                        color = AconTheme.color.Gray1
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        AconFilledLargeButton(
            text = stringResource(R.string.verify_success),
            textStyle = AconTheme.typography.head8_16_sb,
            enabledBackgroundColor = AconTheme.color.Gray5,
            disabledBackgroundColor = AconTheme.color.Gray8,
            enabledTextColor = AconTheme.color.White,
            onClick = onNavigateToNext,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 40.dp)
                .padding(horizontal = 16.dp)
        )
    }
}
