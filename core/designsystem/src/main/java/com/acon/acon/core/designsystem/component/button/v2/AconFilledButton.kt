package com.acon.acon.core.designsystem.component.button.v2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acon.acon.core.designsystem.blur.LocalHazeState
import com.acon.acon.core.designsystem.blur.defaultHazeEffect
import com.acon.acon.core.designsystem.component.loading.AconCircularProgressBar
import com.acon.acon.core.designsystem.theme.AconTheme

@Composable
fun AconFilledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(12.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = AconTheme.color.GlassWhiteDefault,
        contentColor = AconTheme.color.White,
        disabledContainerColor = AconTheme.color.GlassWhiteDisabled,
        disabledContentColor = AconTheme.color.Gray300,
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = PaddingValues(vertical = 15.dp),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable() (RowScope.() -> Unit)
) {

    Button(
        onClick = onClick,
        modifier = modifier.defaultHazeEffect(
            hazeState = LocalHazeState.current,
            tintColor = AconTheme.color.GlassWhiteDefault,
        ),
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    ) {
        if (isLoading) {
            AconCircularProgressBar()
        } else {
            content()
        }
    }
}

@Composable
@Preview
private fun AconFilledButtonPreview() {
    AconTheme {
        AconFilledButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Button",
                style = AconTheme.typography.Title4,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
@Preview
private fun AconFilledButtonLoadingPreview() {
    AconTheme {
        AconFilledButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            isLoading = true,
        ) { }
    }
}