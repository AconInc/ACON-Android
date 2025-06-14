package com.acon.acon.core.designsystem.image

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.designsystem.theme.AconColors
import com.acon.acon.core.designsystem.theme.AconTheme
import com.acon.acon.core.designsystem.theme.AconTypography
import com.acon.acon.core.designsystem.theme.Typography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 이미지 URL을 받아 Bitmap으로 변환
 * @receiver 이미지 URL
 * @param context Context
 * @return Bitmap?
 */
suspend fun String.toBitmap(
    context: Context,
) : Bitmap? {

    var bitmap: Bitmap? = null
    withContext(Dispatchers.IO) {
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(this@toBitmap)
            .allowHardware(false)
            .build()

        val result = imageLoader.execute(request)
        if (result is SuccessResult) {
            bitmap = result.image.toBitmap()
        }
    }

    return bitmap
}

/**
 * 이미지 로딩 실패 시 보여줄 Painter. AsyncImage에서 사용 가능
 * ```
 * AsyncImage(
 *     ...
 *     error = LoadImageErrorPainter.Default
 * )
 *```
 */
class LoadImageErrorPainter(
    private val colors: List<Color>,
    private val icon: Painter,
    private val iconSize: Size,
    private val spacing: Float,
    private val description: String,
    private val textMeasurer: TextMeasurer,
): Painter() {
    override val intrinsicSize: Size = Size.Unspecified

    override fun DrawScope.onDraw() {
        drawRect(
            brush = Brush.verticalGradient(colors = colors)
        )

        val textLayoutResult = textMeasurer.measure(
            text = AnnotatedString(description),
            style = Typography.Body1.copy(
                color = AconColors.Gray50,
                fontWeight = FontWeight.SemiBold
            ),
        )

        val totalContentHeight = iconSize.height + spacing + textLayoutResult.size.height

        val iconTop = center.y - totalContentHeight / 2
        val iconLeft = center.x - iconSize.width / 2

        val textTop = iconTop + iconSize.height + spacing
        val textLeft = center.x - textLayoutResult.size.width / 2

        translate(left = iconLeft, top = iconTop) {
            with(icon) {
                draw(size = iconSize)
            }
        }

        val topLeft = Offset(
            x = textLeft,
            y = textTop
        )

        drawText(textLayoutResult, topLeft = topLeft)
    }

    companion object {
        val Default: LoadImageErrorPainter
            @Composable
            get() = LoadImageErrorPainter(
                colors = listOf(
                    AconColors.Gray900.copy(.6f),
                    AconColors.Gray300.copy(.5f),
                    AconColors.Gray900.copy(.6f)
                ),
                icon = painterResource(R.drawable.ic_acon_fill_white),
                iconSize = with(LocalDensity.current) {
                    Size(36.dp.toPx(), 36.dp.toPx())
                },
                description = stringResource(R.string.fail_to_load_image),
                textMeasurer = rememberTextMeasurer(),
                spacing = with(LocalDensity.current) {
                    12.dp.toPx()
                }
            )
    }
}