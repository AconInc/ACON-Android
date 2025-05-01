package com.acon.acon.navigation.bottom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selectedItem: BottomNavType = BottomNavType.SPOT,
    onItemClick: (BottomNavType) -> Unit = {}
) {

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BottomNavType.entries.fastForEach {
                BottomBarItem(
                    type = it,
                    isSelected = selectedItem == it,
                    modifier = Modifier.weight(1f).noRippleClickable {
                        onItemClick(it)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun BottomBarItem(
    type: BottomNavType,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.padding(top = 8.dp),
            imageVector = ImageVector.vectorResource(
                if (isSelected) type.selectedIconRes else type.unselectedIconRes),
            contentDescription = stringResource(type.titleRes),
            tint = AconTheme.color.White
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(type.titleRes),
            style = AconTheme.typography.body4_12_reg,
            color = AconTheme.color.White
        )
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = AconTheme.color.Dim_b_30)
    )
}