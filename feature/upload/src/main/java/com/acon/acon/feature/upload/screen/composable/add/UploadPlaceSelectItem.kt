package com.acon.acon.feature.upload.screen.composable.add

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.noRippleClickable
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
internal fun UploadPlaceSelectItem(
    title: String,
    modifier: Modifier = Modifier,
    selectedBackgroundColor: Color = AconTheme.color.GraySelected,
    unSelectedBackgroundColor: Color = AconTheme.color.GrayDefault,
    border: BorderStroke = BorderStroke(
        width = 1.dp,
        color = AconTheme.color.GrayBorderSelected
    ),
    shape: Shape = RoundedCornerShape(10.dp),
    isSelected: Boolean = false,
    backgroundColor: Color = if(isSelected) { selectedBackgroundColor } else { unSelectedBackgroundColor },
    onClickUploadPlaceSelectItem: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isSelected) {
                    Modifier.border(border = border, shape = shape)
                } else {
                    Modifier
                }
            )
            .clip(shape = shape)
            .background(backgroundColor)
            .noRippleClickable {
                onClickUploadPlaceSelectItem()
            }
            .padding(vertical = 12.dp, horizontal = 14.dp)
            .padding(start = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = AconTheme.typography.Title4,
            color = AconTheme.color.White,
            fontWeight = FontWeight.Normal
        )

        Spacer(Modifier.weight(1f))
        Image(
            imageVector = ImageVector.vectorResource(
                if (isSelected) {
                    R.drawable.ic_place_selected
                } else {
                    R.drawable.ic_place_un_selected
                }
            ),
            contentDescription = stringResource(R.string.select)
        )
    }
}

@Preview
@Composable
private fun UploadPlaceSelectItemPreView() {
    AconTheme {
        UploadPlaceSelectItem(
            title = "카페"
        )
    }
}