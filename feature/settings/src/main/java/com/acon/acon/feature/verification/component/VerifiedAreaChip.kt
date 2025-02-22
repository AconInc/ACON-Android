package com.acon.acon.feature.verification.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.domain.model.area.Area

@Composable
fun VerifiedAreaChip(
    areaList: List<Area>,
    modifier: Modifier = Modifier,
    onEditArea: (Boolean) -> Unit = {},
    onRemoveChip: (Boolean, Long) -> Unit = { _, _ -> },
) {
    Column(
        modifier = modifier
    ) {
        val chipList = areaList.chunked(2)
        chipList.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                rowItems.forEachIndexed { index, area ->
                    ChipItem(
                        text = area.name,
                        onClickChip = {
                            if (areaList.size <= 1) {
                                onEditArea(true)
                            } else {
                                onRemoveChip(true, area.verifiedAreaId)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = if (index == 1) 8.dp else 0.dp)
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ChipItem(
    text: String,
    modifier: Modifier = Modifier,
    onClickChip: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(
                AconTheme.color.Gray8,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = AconTheme.typography.subtitle1_16_med,
            color = AconTheme.color.White,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        Icon(
            imageVector = ImageVector.vectorResource(
                com.acon.acon.core.designsystem.R.drawable.ic_dissmiss_circle_gray
            ),
            contentDescription = "Remove Icon",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(28.dp)
                .noRippleClickable { onClickChip() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VerifiedAreaChipPreview() {
    val list: List<Area> = listOf(
        Area(1,"망원동"),
        Area(1,"동교동"),
        Area(1,"화곡동")
    )
    AconTheme {
        VerifiedAreaChip(
            areaList = list,
            modifier = Modifier.fillMaxWidth()
        )
    }
}