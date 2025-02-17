package com.acon.acon.feature.profile.composable.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.feature.profile.R

@Composable
fun VerifiedAreaChip(
    modifier : Modifier = Modifier,
    areaList : List<String>,
    onAddArea : () -> Unit,
    onRemoveArea: (String) -> Unit,
    errorMessage: String? = null
) {
    Column(
        modifier = modifier
    ) {
        if (areaList.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // 두 칩 간 균등 간격
            ){
                Button(
                    onClick = onAddArea,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AconTheme.color.Gray9,
                        contentColor = AconTheme.color.White
                    ),
                    border = BorderStroke(width =  1.dp, color = AconTheme.color.Gray5)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "동네 추가하기",
                            style = AconTheme.typography.subtitle1_16_med,
                            color = AconTheme.color.White,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.ic_add_b_24),
                            contentDescription = "Add Icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.padding(horizontal = 2.dp).size(16.dp)
                        )
                    }

                }
                Spacer(modifier = Modifier.weight(1f))
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.and_ic_error_20),
                        contentDescription = "Error Icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(horizontal = 2.dp).size(16.dp)
                    )
                    Text(
                        text = it,
                        style = AconTheme.typography.subtitle2_14_med,
                        color = AconTheme.color.Error_red1
                    )
                }
            }
        } else {
            val chipList = areaList.chunked(2) // 2개씩 묶음
            chipList.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween // 두 칩 간 균등 간격
                ) {
                    rowItems.forEach { area ->
                        ChipItem(
                            text = area,
                            onRemoveArea = { onRemoveArea(area) },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                    }

                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp)) // 행 간 간격
            }
        }
    }
}

@Composable
fun ChipItem(
    text: String,
    onRemoveArea: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(AconTheme.color.Gray7, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = AconTheme.typography.subtitle1_16_med,
            color = AconTheme.color.White,
            modifier = Modifier.weight(1f).padding(end = 8.dp)
        )
        IconButton(onClick = onRemoveArea) {
            Icon(
                imageVector = ImageVector.vectorResource(com.acon.acon.core.designsystem.R.drawable.ic_dissmiss_circle_gray),
                contentDescription = "Remove Icon",
                tint = Color.Unspecified,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VerifiedAreaChipPreview() {
    var areaList by remember { mutableStateOf(listOf<String>()) }

    VerifiedAreaChip(
        areaList = areaList,
        onAddArea = { areaList = listOf("쌍문동") },
        onRemoveArea = { area -> areaList = areaList.filter { it != area } },
        errorMessage = if (areaList.isEmpty()) "로컬토리를 위해 최소 1개의 동네를 인증해주세요" else null
    )
}
