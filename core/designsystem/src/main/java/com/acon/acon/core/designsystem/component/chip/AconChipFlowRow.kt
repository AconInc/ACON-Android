package com.acon.acon.core.designsystem.component.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AconChipFlowRow(
    titles: List<String>,
    vararg selectedChipIndexes: Int,
    onChipSelected: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(6.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(6.dp)
) {

    FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        titles.fastForEachIndexed { index, title ->
            AconChip(
                title = title,
                isSelected = selectedChipIndexes.contains(index),
                onClick = { onChipSelected(index) }
            )
        }
    }
}