package com.acon.acon.core.designsystem.component.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconTag(
    text: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    textStyle: TextStyle = AconTheme.typography.Caption1.copy(fontWeight = FontWeight.W400),
    contentPadding: PaddingValues = PaddingValues(vertical = 3.dp, horizontal = 15.dp)
) {
    Row(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = shape
            ).padding(contentPadding),
    ) {
        Text(
            text = text,
            style = textStyle,
            color = AconTheme.color.White
        )
    }
}

@Composable
@Preview
private fun AconTagPreview() {
    AconTag(
        text = "Tag",
        backgroundColor = AconTheme.color.TagNew,
    )
}