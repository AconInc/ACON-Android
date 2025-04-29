package com.acon.acon.core.designsystem.component.button.v2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconFilledTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = AconTheme.typography.Title4.copy(fontWeight = FontWeight.SemiBold),
    isLoading: Boolean = false,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(12.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = AconTheme. color. GlassWhiteDefault,
        contentColor = AconTheme. color. White,
        disabledContainerColor = AconTheme. color. GlassWhiteDisabled,
        disabledContentColor = AconTheme. color. Gray300
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = PaddingValues(vertical = 15.dp),
    interactionSource: MutableInteractionSource? = null,
) {
    AconFilledButton(
        onClick = onClick,
        modifier = modifier,
        isLoading = isLoading,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    ) {
        Text(
            text = text,
            style = textStyle
        )
    }
}

@Composable
@Preview
private fun PreviewAconFilledTextButton() {
    AconFilledTextButton(
        text = "Text Button",
        onClick = { },
        modifier = Modifier.fillMaxWidth(),
        isLoading = false,
    )
}