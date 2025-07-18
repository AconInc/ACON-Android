package com.acon.acon.core.designsystem.component.bottombar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

enum class BottomNavType(
    @StringRes val titleRes: Int,
    @DrawableRes val selectedIconRes: Int,
    @DrawableRes val unselectedIconRes: Int
) {
    SPOT(R.string.title_spot, R.drawable.ic_starlight_filled, R.drawable.ic_starlight),
    UPLOAD(R.string.title_upload, R.drawable.ic_upload , R.drawable.ic_upload),
    PROFILE(R.string.title_profile, R.drawable.ic_profile_filled, R.drawable.ic_profile)
}

@Composable
fun AconBottomBar(
    selectedItem: BottomNavType,
    onItemClick: (bottomType: BottomNavType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        BottomNavType.entries.fastForEach { bottomType ->
            BottomBarItem(
                type = bottomType,
                isSelected = selectedItem == bottomType,
                modifier = Modifier
                    .weight(1f)
                    .noRippleClickable {
                        onItemClick(bottomType)
                    }
                    .padding(bottom = 24.dp)
            )
        }
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
private fun BottomBarPreview() {
    AconBottomBar(
        selectedItem = BottomNavType.SPOT,
        onItemClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .background(color = AconTheme.color.Dim_b_30)
    )
}
