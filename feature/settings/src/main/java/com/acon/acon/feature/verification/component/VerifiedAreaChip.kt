package com.acon.acon.feature.verification.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.area.Area

@Composable
fun VerifiedAreaChip(
    areaList: List<Area>,
    onEditArea: () -> Unit,
    onRemoveChip: (Long) -> Unit,
    onClickAddArea: () -> Unit,
    maxChipPerRowSize: Int = 3
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        areaList.forEach { area ->
            LocalAreaChip(
                text = area.name,
                onClickChip = {
                    if (areaList.size <= 1) {
                        onEditArea()
                    } else {
                        onRemoveChip(area.verifiedAreaId)
                    }
                }
            )
        }
        if (areaList.size < maxChipPerRowSize) {
            AddAreaChip(onClickAddArea = onClickAddArea)
        }
    }
}

@Composable
fun LocalAreaChip(
    text: String,
    onClickChip: () -> Unit,
) {
    Row(
        modifier = Modifier
            .background(
                AconTheme.color.GlassWhiteDefault,
                shape = CircleShape
            )
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = AconTheme.typography.Body1,
            color = AconTheme.color.White
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_clear),
            contentDescription = stringResource(R.string.content_description_delete_area),
            tint = AconTheme.color.Gray50,
            modifier = Modifier
                .padding(start = 10.dp)
                .padding(vertical = 1.dp)
                .noRippleClickable { onClickChip() }
        )
    }
}

@Composable
fun AddAreaChip(
    onClickAddArea: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                AconTheme.color.Gray900,
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = AconTheme.color.GlassWhiteDefault,
                shape = CircleShape
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .noRippleClickable { onClickAddArea() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.settings_area_verification_add_area),
            style = AconTheme.typography.Body1,
            color = AconTheme.color.Action
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VerifiedAreaChipPreview() {
    val list: List<Area> = listOf(
        Area(1, "망원동"),
        Area(1, "을지로4가")
    )
    AconTheme {
        Box(
            modifier = Modifier
                .background(AconTheme.color.Gray900)
        ) {
            VerifiedAreaChip(
                areaList = list,
                onClickAddArea = {},
                onRemoveChip = {},
                onEditArea = {}
            )
        }
    }
}