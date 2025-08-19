package com.acon.acon.feature.spot.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.effect.LocalHazeState
import com.acon.acon.core.designsystem.effect.defaultHazeEffect
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.model.type.SpotType

@Composable
internal fun SpotTypeToggle(
    selectedType: com.acon.acon.core.model.type.SpotType,
    onSwitched: (com.acon.acon.core.model.type.SpotType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .defaultHazeEffect(
                hazeState = LocalHazeState.current,
                tintColor = AconTheme.color.Gray800
            ).height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SpotTypeToggleItem(
            spotType = com.acon.acon.core.model.type.SpotType.RESTAURANT,
            isSelected = selectedType == com.acon.acon.core.model.type.SpotType.RESTAURANT,
            onClick = { onSwitched(com.acon.acon.core.model.type.SpotType.RESTAURANT) }
        )
        SpotTypeToggleItem(
            spotType = com.acon.acon.core.model.type.SpotType.CAFE,
            isSelected = selectedType == com.acon.acon.core.model.type.SpotType.CAFE,
            onClick = { onSwitched(com.acon.acon.core.model.type.SpotType.CAFE) }
        )
    }
}

@Composable
private fun SpotTypeToggleItem(
    spotType: com.acon.acon.core.model.type.SpotType,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(50))
            .background(
                color = if (isSelected) AconTheme.color.White else Color.Transparent,
            ).padding(horizontal = 16.dp, vertical = 6.dp)
            .noRippleClickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(
                if (spotType == com.acon.acon.core.model.type.SpotType.RESTAURANT) R.drawable.ic_restaurant else R.drawable.ic_cafe
            ),
            contentDescription = stringResource(if (spotType == com.acon.acon.core.model.type.SpotType.RESTAURANT) R.string.restaurant else R.string.cafe),
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
        Text(
            text = stringResource(if (spotType == com.acon.acon.core.model.type.SpotType.RESTAURANT) R.string.restaurant else R.string.cafe),
            style = AconTheme.typography.Caption1,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.W400,
            color = if (isSelected) AconTheme.color.Gray900 else AconTheme.color.Gray300,
        )
    }
}

@Preview
@Composable
private fun SpotTypeTogglePreview1() {
    AconTheme {
        SpotTypeToggle(
            selectedType = com.acon.acon.core.model.type.SpotType.RESTAURANT,
            onSwitched = {},
            modifier = Modifier
        )
    }
}
@Preview
@Composable
private fun SpotTypeTogglePreview2() {
    AconTheme {
        SpotTypeToggle(
            selectedType = com.acon.acon.core.model.type.SpotType.CAFE,
            onSwitched = {},
            modifier = Modifier
        )
    }
}